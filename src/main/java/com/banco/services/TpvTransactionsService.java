package com.banco.services;

import com.banco.entities.TpvTransactions;
import com.banco.exceptions.CustomException;

import java.util.List;

public interface TpvTransactionsService {
    List<TpvTransactions> getTpvTransactionsInfo() throws CustomException;
}
