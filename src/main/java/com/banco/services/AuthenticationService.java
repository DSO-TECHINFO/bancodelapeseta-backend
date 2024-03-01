package com.banco.services;

import com.banco.dtos.*;
import com.banco.exceptions.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import java.io.IOException;


public interface AuthenticationService {

    AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto, HttpServletRequest request) throws CustomException, IOException;

    @Transactional
    void registerPhysical(RegisterPhysicalDto registerPhysicalDto, HttpServletRequest request) throws CustomException;

    @Transactional
    void registerCompany(RegisterCompanyDto registerCompanyDto, HttpServletRequest request) throws CustomException;

    void passwordChange(PasswordChangeDto passwordChangeDto) throws CustomException;

    void emailChange(EmailChangeDto emailChangeDto) throws CustomException;

    void phoneChange(PhoneChangeDto phoneChangeDto) throws CustomException;

    void signCreateOrModify(SignCreateDto signCreateDto) throws CustomException;

    void recoveryPassword(RecoveryPasswordDto recoveryPasswordDto) throws CustomException;

    void recoveryPasswordChange(RecoveryPasswordChangeDto recoveryPasswordChangeDto) throws CustomException;

    RecoveryPasswordCodeReturnDto recoveryPasswordCheckCode(RecoveryPasswordCodeInputDto recoveryPasswordCodeInputDto) throws CustomException;
}
