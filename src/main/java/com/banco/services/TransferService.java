package com.banco.services;

import com.banco.dtos.TransferInputDto;

public interface TransferService {
    void createTransfer(TransferInputDto transferInputDto);
}
