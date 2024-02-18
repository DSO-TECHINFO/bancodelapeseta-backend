package com.banco.services;


import com.banco.dtos.AuthenticationRequestDto;
import com.banco.dtos.AuthenticationResponseDto;
import com.banco.dtos.RegisterCompanyDto;
import com.banco.dtos.RegisterPhysicalDto;
import com.banco.entities.Entity;
import com.banco.entities.EntityDebtType;
import com.banco.entities.EntityType;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;
import com.banco.repositories.RoleRepository;
import com.banco.security.JwtService;
import com.banco.utils.NonNullFields;
import com.banco.utils.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.random.RandomGenerator;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    private final AuthenticationManager authenticationManager;
    private final EntityRepository entityRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordUtils passwordUtils;
    private final NonNullFields nonNullFields;
    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto) throws CustomException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            Optional<Entity> user = entityRepository.findByTaxId(authenticationRequestDto.getUsername());
            if (user.isEmpty())
                throw new CustomException("USERS-001", "Wrong username or password", 404);
            Entity finalEntity = user.get();
            if (finalEntity.getLoginAttempts() == null)
                finalEntity.setLoginAttempts((short) 0);
            short attempts = finalEntity.getLoginAttempts();
            if (attempts < 3) {
                finalEntity.setLoginAttempts(++attempts);
                entityRepository.save(finalEntity);
                throw new CustomException("USERS-001", "Wrong username or password", 401);
            } else {
                finalEntity.setLocked(true);
                finalEntity.setLastAttempt(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 10));
                entityRepository.save(finalEntity);
                throw new CustomException("USERS-002", "User is locked, try again in: 10 minutes", 401);
            }
        } catch (LockedException e) {
            Entity entity = entityRepository.findByTaxId(authenticationRequestDto.getUsername()).orElseThrow();
            if (entity.getLastAttempt().before(new Timestamp(System.currentTimeMillis()))) {
                entity.setLocked(false);
                entity.setLastAttempt(null);
                entity.setLoginAttempts((short) 0);
                entityRepository.save(entity);
                return this.login(authenticationRequestDto);
            } else {
                long diffInSecs = Duration.between(Instant.now(), entity.getLastAttempt().toInstant())
                        .getSeconds();
                long minutes = Math.floorDiv(diffInSecs, 60L);
                long secs = diffInSecs % 60;
                throw new CustomException("USERS-004", "User is locked, try again in: " + minutes + " minutes and " + secs + " seconds", 401);
            }
        } catch (DisabledException e) {
            throw new CustomException("USERS-005", "Confirm your email and phone to continue, you can resend email and phone code.", 401);
        }
        Entity entity = entityRepository.findByTaxId(authenticationRequestDto.getUsername()).orElseThrow();
        entity.setLoginAttempts((short) 0);
        entityRepository.save(entity);
        return AuthenticationResponseDto.builder().token(jwtService.generateToken(entity)).build();
    }

    @Transactional
    @Override
    public void registerPhysical(RegisterPhysicalDto registerPhysicalDto, HttpServletRequest request) throws CustomException {
        Entity entity = Entity.builder()
                .creationDate(new Date(System.currentTimeMillis()))
                .emailConfirmed(false)
                .phoneConfirmed(false)
                .locked(false)
                .signActivated(false)
                .type(EntityType.PHYSICAL)
                .loginAttempts((short)0)
                .employee(false)
                .lastAttempt(new Date())
                .build();

        if(!passwordUtils.checkPasswordValid(registerPhysicalDto.getPassword())){
            throw new CustomException("USERS-006", "Password does not fit password requirements", 400);
        }
        if(registerPhysicalDto.getDebtType() != EntityDebtType.FREELANCE
                && registerPhysicalDto.getDebtType() != EntityDebtType.STATE_WORKER
                && registerPhysicalDto.getDebtType() != EntityDebtType.SALARIED
                && registerPhysicalDto.getDebtType() != EntityDebtType.PENSIONER ){
            throw new CustomException("USERS-007", "Physical person cannot have company debt type", 400);
        }
        if(registerPhysicalDto.getNationalIdExpiration().after(new Date()))
            throw new CustomException("USERS-008", "You national document has expirated.", 400);

        nonNullFields.copyNonNullProperties(registerPhysicalDto, entity, true);
        entity.setCreatedIpAddress(request.getRemoteAddr());
        entity.setType(EntityType.PHYSICAL);
        entity.setPassword(passwordEncoder.encode(registerPhysicalDto.getPassword()));
        entityRepository.save(entity);
    }
    @Transactional
    @Override
    public void registerCompany(RegisterCompanyDto registerCompanyDto, HttpServletRequest request) throws CustomException {
        Entity entity = Entity.builder()
                .creationDate(new Date(System.currentTimeMillis()))
                .emailConfirmed(false)
                .phoneConfirmed(false)
                .locked(false)
                .signActivated(false)
                .type(EntityType.COMPANY)
                .loginAttempts((short)0)
                .employee(false)
                .lastAttempt(new Date())
                .build();

        if(!passwordUtils.checkPasswordValid(registerCompanyDto.getPassword())){
            throw new CustomException("USERS-009", "Password does not fit password requirements", 400);
        }
        if(registerCompanyDto.getDebtType() != EntityDebtType.PYME
                && registerCompanyDto.getDebtType() != EntityDebtType.MICROCOMPANY
                && registerCompanyDto.getDebtType() != EntityDebtType.STARTUP
                && registerCompanyDto.getDebtType() != EntityDebtType.COMPANY ){
            throw new CustomException("USERS-010", "Company canoot have physical person debt type", 400);
        }
        if(registerCompanyDto.getSettingUpDate().after(new Date()))
            throw new CustomException("USERS-011", "Company set up date cannot be a date after today", 400);


        nonNullFields.copyNonNullProperties(registerCompanyDto, entity, true);
        entity.setCreatedIpAddress(request.getRemoteAddr());
        entity.setType(EntityType.COMPANY);
        entity.setPassword(passwordEncoder.encode(registerCompanyDto.getPassword()));
        entityRepository.save(entity);
    }

}
