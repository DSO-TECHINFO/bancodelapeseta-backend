package com.banco.services;

import com.banco.entities.Loan;
import com.banco.exceptions.CustomException;

import java.util.List;

public interface LoanService {
    List<Loan> getLoans() throws CustomException;
}
