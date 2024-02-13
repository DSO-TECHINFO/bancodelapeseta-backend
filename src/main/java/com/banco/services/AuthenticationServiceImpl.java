package com.banco.services;


import com.banco.dtos.AuthenticationRequestDto;
import com.banco.dtos.AuthenticationResponseDto;
import com.banco.dtos.RegisterRequestDto;
import com.banco.entities.User;
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
            Optional<User> user = userRepository.findByUsername(authenticationRequestDto.getUsername());
            if (user.isEmpty())
                throw new CustomException("USERS-004", "User not found", 404);
            User finalUser = user.get();
            if (finalUser.getAttempts() == null)
                finalUser.setAttempts((short) 0);
            short attempts = finalUser.getAttempts();
            if (attempts < 3) {
                finalUser.setAttempts(++attempts);
                userRepository.save(finalUser);
                throw new CustomException("USERS-0006", "Wrong password, attempts left: " + (4 - attempts), 401);
            } else {
                finalUser.setLocked(true);
                finalUser.setLastAttempt(new Timestamp(System.currentTimeMillis() + 1000 * 60 * 10));
                userRepository.save(finalUser);
                throw new CustomException("USERS-005", "User is locked, try again in: 10 minutes", 401);
            }
        } catch (LockedException e) {
            User user = userRepository.findByUsername(authenticationRequestDto.getUsername()).orElseThrow();
            if (user.getLastAttempt().before(new Timestamp(System.currentTimeMillis()))) {
                user.setLocked(false);
                user.setLastAttempt(null);
                user.setAttempts((short) 0);
                userRepository.save(user);
                return this.login(authenticationRequestDto);
            } else {
                long diffInSecs = Duration.between(Instant.now(), user.getLastAttempt().toInstant())
                        .getSeconds();
                long minutes = Math.floorDiv(diffInSecs, 60L);
                long secs = diffInSecs % 60;
                throw new CustomException("USERS-006", "User is locked, try again in: " + minutes + " minutes and " + secs + " seconds", 401);
            }
        } catch (DisabledException e) {
            throw new CustomException("USERS-007", "Confirm your email to continue, you can resend email link.", 401);
        }
        User user = userRepository.findByUsername(authenticationRequestDto.getUsername()).orElseThrow();
        user.setAttempts((short) 0);
        userRepository.save(user);
        return AuthenticationResponseDto.builder().token(jwtService.generateToken(user)).refresh_token(jwtService.generateRefreshToken(user)).build();
    }

    @Transactional
    @Override
    public void register(RegisterRequestDto registerRequestDto, HttpServletRequest request) throws CustomException {
        if (!passwordUtils.checkPasswordValid(registerRequestDto.getPassword())) {
            throw new CustomException("USERS-003", "Password does not fit password requirements", 400);
        }
        User user = User.builder()
                .email(registerRequestDto.getEmail())
                .username(registerRequestDto.getUsername())
                .password(passwordEncoder.encode(registerRequestDto.getPassword()))
                .role(roleRepository.findAll().stream().filter(role -> role.getRoleName().equals("ROLE_USER")).findFirst().orElseThrow())
                .locked(false)
                .phoneNumber(registerRequestDto.getPhone())
                .attempts((short)0)
                .emailConfirmed(false)
                .creationDate(new Date(System.currentTimeMillis()))
                .build();
        userRepository.save(user);
    }
}
