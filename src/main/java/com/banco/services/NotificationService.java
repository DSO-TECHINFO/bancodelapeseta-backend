package com.banco.services;

import com.banco.entities.EmailType;
import com.banco.entities.Entity;
import com.banco.entities.SMSType;
import com.banco.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

public interface NotificationService {

    void sendEmailVerificationCode() throws CustomException;

    void sendPhoneVerificationCode() throws CustomException;
    void sendAccountDataModification() throws CustomException;
    void sendCardCharge() throws CustomException;
    void sendCompletedLoan() throws CustomException;
    void sendCreatedNewBankAccount() throws CustomException;
    void sendCreatedNewCard() throws CustomException;
    void sendCreatedNewLoan() throws CustomException;
    void sendEmailModification() throws CustomException;
    void sendNewLogin(Entity entity,String address) throws CustomException;
    void sendRecalculatedLoan() throws CustomException;
    void sendSignModification() throws CustomException;
    void sendTransactionVerification() throws CustomException;
    void sendTransferReceived() throws CustomException;
    void sendTransferSent() throws CustomException;
    void sendUnpaidLoanSubscription() throws CustomException;
    void sendWelcome() throws CustomException;
}
