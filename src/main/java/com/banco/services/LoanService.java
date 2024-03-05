package com.banco.services;

import java.util.ArrayList;
import java.util.List;

import com.banco.dtos.ListingLoanDto;
import com.banco.entities.Loan;
import com.banco.exceptions.CustomException;
import com.banco.repositories.LoanRepository;

public interface LoanService {

    List<ListingLoanDto> getUserLoans() throws CustomException;

    static List<ListingLoanDto> loanListToLoanDtoList(List<Loan> loanList) {
        List<ListingLoanDto> loanListDto = new ArrayList<>();
        for (Loan loan : loanList) {
            loanListDto.add(loanToLoanDto(loan));
        }
        return loanListDto;
    }

    static ListingLoanDto loanToLoanDto(Loan loan) {
        return ListingLoanDto.builder()
                .contractId(loan.getContract().getId().toString())
                .totalAmount(loan.getAmount().toString())
                .amount(loan.getTotalAmount().toString())
                .interestRate(loan.getInterestRate().toString())
                .build();
    }
}
