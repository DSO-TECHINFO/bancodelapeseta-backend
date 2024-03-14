package com.banco.services;

import com.banco.dtos.LoanDto;
import com.banco.dtos.LoanRequestDto;
import com.banco.entities.Loan;
import com.banco.exceptions.CustomException;

import java.util.List;

public interface LoanService {
    List<LoanDto> getLoans() throws CustomException;

    public void loanCreation(LoanRequestDto loanRequestDto, Long accountId, Long productId);
}
