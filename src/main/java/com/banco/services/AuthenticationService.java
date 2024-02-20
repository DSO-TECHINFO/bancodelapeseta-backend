package com.banco.services;

import com.banco.dtos.AuthenticationRequestDto;
import com.banco.dtos.AuthenticationResponseDto;
import com.banco.dtos.RegisterCompanyDto;
import com.banco.dtos.RegisterPhysicalDto;
import com.banco.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;


public interface AuthenticationService {

    AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto, HttpServletRequest request) throws CustomException;

    @Transactional
    void registerPhysical(RegisterPhysicalDto registerPhysicalDto, HttpServletRequest request) throws CustomException;

    @Transactional
    void registerCompany(RegisterCompanyDto registerCompanyDto, HttpServletRequest request) throws CustomException;
}
