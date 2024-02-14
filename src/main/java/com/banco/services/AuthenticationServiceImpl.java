package com.banco.services;


import com.banco.dtos.AuthenticationRequestDto;
import com.banco.dtos.AuthenticationResponseDto;
import com.banco.dtos.RegisterRequestDto;
import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.repositories.RoleRepository;
import com.banco.repositories.UserRepository;
import com.banco.security.JwtService;
import com.banco.utils.PasswordUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordUtils passwordUtils;

    @Override
    public AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto) throws CustomException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequestDto.getUsername(), authenticationRequestDto.getPassword()));
        } catch (BadCredentialsException e) {
            Optional<Entity> user = userRepository.findByUsername(authenticationRequestDto.getUsername());
            if (user.isEmpty())
                throw new CustomException("USERS-004", "User not found", 404);
            Entity finalEntity = user.get();
            if (finalEntity.getLoginAttempts() == null)
                finalEntity.setLoginAttempts((short) 0);
            short attempts = finalEntity.getLoginAttempts();
            if (attempts < 3) {
                finalEntity.setLoginAttempts(++attempts);
                userRepository.save(finalEntity);
                throw new CustomException("USERS-0006", "Wrong password, attempts left: " + (4 - attempts), 401);
            } else {
                finalEntity.setLocked(true);
                finalEntity.setLastAttempt(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 10));
                userRepository.save(finalEntity);
                throw new CustomException("USERS-005", "User is locked, try again in: 10 minutes", 401);
            }
        } catch (LockedException e) {
            Entity entity = userRepository.findByUsername(authenticationRequestDto.getUsername()).orElseThrow();
            if (entity.getLastAttempt().before(new Timestamp(System.currentTimeMillis()))) {
                entity.setLocked(false);
                entity.setLastAttempt(null);
                entity.setLoginAttempts((short) 0);
                userRepository.save(entity);
                return this.login(authenticationRequestDto);
            } else {
                long diffInSecs = Duration.between(Instant.now(), entity.getLastAttempt().toInstant())
                        .getSeconds();
                long minutes = Math.floorDiv(diffInSecs, 60L);
                long secs = diffInSecs % 60;
                throw new CustomException("USERS-006", "User is locked, try again in: " + minutes + " minutes and " + secs + " seconds", 401);
            }
        } catch (DisabledException e) {
            throw new CustomException("USERS-007", "Confirm your email to continue, you can resend email link.", 401);
        }
        Entity entity = userRepository.findByUsername(authenticationRequestDto.getUsername()).orElseThrow();
        entity.setLoginAttempts((short) 0);
        userRepository.save(entity);
        return AuthenticationResponseDto.builder().token(jwtService.generateToken(entity)).refresh_token(jwtService.generateRefreshToken(entity)).build();
    }

    @Transactional
    @Override
    public void register(RegisterRequestDto registerRequestDto, HttpServletRequest request) throws CustomException {
        if (!passwordUtils.checkPasswordValid(registerRequestDto.getPassword())) {
            throw new CustomException("USERS-003", "Password does not fit password requirements", 400);
        }
        Entity entity = Entity.builder()
                .email(registerRequestDto.getEmail())
                .name(registerRequestDto.getName())
                .name(registerRequestDto.getSurname())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(roleRepository.findAll().stream().filter(role -> role.getRoleName().equals("ROLE_USER")).findFirst().orElseThrow())
                .locked(false)
                .phoneNumber(registerRequestDto.getPhone())
                .loginAttempts((short)0)
                .emailConfirmed(false)
                .creationDate(new Date(System.currentTimeMillis()))
                .build();
        userRepository.save(entity);
    }
}
