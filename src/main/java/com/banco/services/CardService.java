package com.banco.services;

import com.banco.dtos.TransactionVerificationDto;
import com.banco.exceptions.CustomException;

public interface CardService {
    void accessSensitiveInfo(TransactionVerificationDto transactionVerificationDto) throws CustomException;
}
