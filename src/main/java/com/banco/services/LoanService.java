package com.banco.services;

import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;

import java.util.List;

public interface LoanService {
    List<EntityContract> getLoans() throws CustomException;
}
