package com.banco.services;

import com.banco.dtos.AuthenticationRequestDto;
import com.banco.dtos.AuthenticationResponseDto;
import com.banco.dtos.RegisterRequestDto;
import com.banco.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;


public interface AuthenticationService {

    AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto) throws CustomException;


    @Transactional
    void register(RegisterRequestDto registerRequestDto, HttpServletRequest request) throws CustomException;
}
