package com.banco.Factories;

import com.banco.entities.Loan;



import java.math.BigDecimal;

public class LoanFactory{

    public Loan sampleLoan (){
        return Loan.builder()
                .loanNumberPayments(12)
                .amount(new BigDecimal("10000"))
                .interestRate(new BigDecimal ("40")).build();
    }

    public Loan sampleCustomLoan(int loanNumberPayments, BigDecimal amount, BigDecimal interestRate){
        return Loan.builder()
                .loanNumberPayments(loanNumberPayments)
                .amount(amount)
                .interestRate(interestRate)
                .build();
    }


}
