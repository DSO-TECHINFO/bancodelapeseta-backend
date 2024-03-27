package com.banco.services;


import com.banco.dtos.CancelTransferDto;
import com.banco.dtos.CreateTransferDto;
import com.banco.entities.Transfer;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;
//aa
public interface TransferService {
    Map<String, List<Transfer>> listTransfers();

    @Transactional
    void createTransfer(CreateTransferDto createTransferDto);

    void cancelTransfer(CancelTransferDto cancelTransferDto);
}
