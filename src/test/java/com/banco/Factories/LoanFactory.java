package com.banco.Factories;

import com.banco.entities.Loan;

import java.math.BigDecimal;

public class LoanFactory {

    public Loan sampleLoan (){
        return Loan.builder()
                .loanNumberPayments(12)
                .amount(new BigDecimal("10000"))
                .interestRate(new BigDecimal ("0.40")).build();
    }


}
