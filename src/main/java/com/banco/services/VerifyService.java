package com.banco.services;

import com.banco.dtos.EmailPhoneVerificationDto;
import com.banco.dtos.TransactionVerificationDto;
import com.banco.dtos.VerificationCodeReturnDto;
import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;

public interface VerifyService {

    void verifyEmail(EmailPhoneVerificationDto emailPhoneVerificationDto) throws CustomException;
    void verifyPhone(EmailPhoneVerificationDto emailPhoneVerificationDto) throws CustomException;

    Entity verifyWithEmailCode(String emailCode) throws CustomException;
    Entity verifyWithPhoneCode(String phoneCode) throws CustomException;
    Entity verifyWithSign(String sign) throws CustomException;
    VerificationCodeReturnDto verifyTransaction(TransactionVerificationDto transactionVerificationDto) throws CustomException;
    VerificationCodeReturnDto verifyTransactionWithSign(TransactionVerificationDto transactionVerificationDto) throws CustomException;

    boolean verifyTransactionCode(String transactionCode, Boolean doesTransactionNeedsToBeSigned) throws CustomException;
}
