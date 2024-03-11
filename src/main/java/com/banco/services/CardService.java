package com.banco.services;

import com.banco.dtos.CardCredentialsDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;

import java.util.List;

public interface CardService {
    List<EntityContract> getUserCards() throws CustomException; 
    CardCredentialsDto getCredentials(String cardNumber, VerificationCodeDto verificationCodeDto) throws CustomException;
    void deactivateCard(String cardNumber) throws CustomException;

}
