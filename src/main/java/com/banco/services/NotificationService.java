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
}
