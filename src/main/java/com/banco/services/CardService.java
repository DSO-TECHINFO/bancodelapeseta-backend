package com.banco.services;

import java.util.List;

import com.banco.dtos.CardDto;
import com.banco.dtos.TransactionVerificationDto;
import com.banco.exceptions.CustomException;

public interface CardService {
    void accessSensitiveInfo(TransactionVerificationDto transactionVerificationDto) throws CustomException;
    List<CardDto> getUserCards() throws CustomException; 
}
