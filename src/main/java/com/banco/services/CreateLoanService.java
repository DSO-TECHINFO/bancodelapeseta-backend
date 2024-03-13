package com.banco.services;


import com.banco.dtos.LoanRequestDto;
import com.banco.entities.Loan;

public interface CreateLoanService {

    public void loanCreation(LoanRequestDto loanRequestDto);

}
