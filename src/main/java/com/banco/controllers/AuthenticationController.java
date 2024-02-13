package com.banco.controllers;

import com.banco.dtos.AuthenticationRequestDto;
import com.banco.dtos.RegisterRequestDto;
import com.banco.exceptions.CustomException;
import com.banco.services.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
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
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto authenticationRequestDto) throws CustomException {
        return ResponseEntity.ok(authenticationService.login(authenticationRequestDto));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto, HttpServletRequest request) throws CustomException {
        authenticationService.register(registerRequestDto, request);
        return ResponseEntity.ok().build();
    }

}
