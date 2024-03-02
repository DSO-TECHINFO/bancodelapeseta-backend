package com.banco.services;

import com.banco.dtos.EmailPhoneCodeExpirationReturn;
import com.banco.entities.EmailType;
import com.banco.entities.Entity;
import com.banco.entities.SMSType;
import com.banco.exceptions.CustomException;

import java.io.IOException;
import java.util.Map;

public interface NotificationService {

    EmailPhoneCodeExpirationReturn sendEmailVerificationCode() throws CustomException;

    EmailPhoneCodeExpirationReturn sendPhoneVerificationCode() throws CustomException;

    void sendAccountDataModification() throws CustomException;
    void sendCardCharge() throws CustomException;
    void sendCompletedLoan() throws CustomException;
    void sendCreatedNewBankAccount() throws CustomException;
    void sendCreatedNewCard() throws CustomException;
    void sendCreatedNewLoan() throws CustomException;
    void sendEmailModification() throws CustomException;
    void sendNewLogin(Entity entity,String address) throws CustomException, IOException;
    void sendRecalculatedLoan() throws CustomException;
    void sendSignModification() throws CustomException;
    void sendTransactionVerification() throws CustomException;
    void sendTransferReceived() throws CustomException;
    void sendTransferSent() throws CustomException;
    void sendUnpaidLoanSubscription() throws CustomException;
    void sendWelcome() throws CustomException;

    void sendMail(Entity entity, Map<String, Object> keysToReplace, EmailType emailType) throws CustomException;

    void sendSMS(Map<String, Object> keysToReplace, Entity entity, SMSType smsType) throws CustomException;
}
