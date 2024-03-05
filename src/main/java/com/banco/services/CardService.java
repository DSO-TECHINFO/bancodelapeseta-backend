package com.banco.services;

import java.util.ArrayList;
import java.util.List;

import com.banco.dtos.CardCredentialsDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;

public interface CardService {
    List<EntityContract> getUserCards() throws CustomException; 
    CardCredentialsDto getCredentials(String cardNumber, VerificationCodeDto verificationCodeDto) throws CustomException;
}
