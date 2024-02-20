package com.banco.controllers;

import com.banco.dtos.AuthenticationRequestDto;
import com.banco.dtos.RegisterCompanyDto;
import com.banco.dtos.RegisterPhysicalDto;
import com.banco.exceptions.CustomException;
import com.banco.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@AllArgsConstructor
@RestController
@RequestMapping(path = "/auth")
public class AuthenticationController {

    public final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto authenticationRequestDto, HttpServletRequest request) throws CustomException {
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

}
