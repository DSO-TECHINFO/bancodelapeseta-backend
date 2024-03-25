package com.banco.controllers;

import com.banco.dtos.*;
import com.banco.exceptions.CustomException;
import com.banco.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@AllArgsConstructor
@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    public final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto authenticationRequestDto, HttpServletRequest request) throws CustomException, IOException {
        return ResponseEntity.ok(authenticationService.login(authenticationRequestDto, request));
    }

    @PostMapping("/register/physical")
    public ResponseEntity<?> register(@RequestBody RegisterPhysicalDto registerPhysicalDto, HttpServletRequest request) throws CustomException {
        authenticationService.registerPhysical(registerPhysicalDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/register/company")
    public ResponseEntity<?> register(@RequestBody RegisterCompanyDto registerCompanyDto, HttpServletRequest request) throws CustomException {
        authenticationService.registerCompany(registerCompanyDto, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/password/change")
    public ResponseEntity<?> passwordChange(@RequestBody PasswordChangeDto passwordChangeDto) throws CustomException {
        authenticationService.passwordChange(passwordChangeDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/recovery/password")
    public ResponseEntity<?> recoveryPassword(@RequestBody RecoveryPasswordDto recoveryPasswordDto) throws CustomException {
        authenticationService.recoveryPassword(recoveryPasswordDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/recovery/password/check/code")
    public ResponseEntity<?> recoveryPasswordCheckCode(@RequestBody RecoveryPasswordCodeInputDto recoveryPasswordCodeInputDto) throws CustomException {
        return ResponseEntity.ok(authenticationService.recoveryPasswordCheckCode(recoveryPasswordCodeInputDto));
    }
    @PostMapping("/recovery/password/change")
    public ResponseEntity<?> recoveryPasswordChange(@RequestBody RecoveryPasswordChangeDto recoveryPasswordChangeDto) throws CustomException {
        authenticationService.recoveryPasswordChange(recoveryPasswordChangeDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change/email")
    public ResponseEntity<?> emailChange(@RequestBody EmailChangeDto emailChangeDto) throws CustomException {
        authenticationService.emailChange(emailChangeDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/change/phone")
    public ResponseEntity<?> phoneChange(@RequestBody PhoneChangeDto phoneChangeDto) throws CustomException {
        authenticationService.phoneChange(phoneChangeDto);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/create/modify/sign")
    public ResponseEntity<?> createSign(@RequestBody SignCreateDto signCreateDto) throws CustomException {
        authenticationService.signCreateOrModify(signCreateDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current/user")
    public ResponseEntity<?> getCurrentUser(){
        return ResponseEntity.ok(authenticationService.getCurrentUser());
    }
}
