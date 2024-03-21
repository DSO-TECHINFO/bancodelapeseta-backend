package com.banco.Factories;

import com.banco.entities.*;


import java.math.BigDecimal;
import java.util.Date;

public class LoanFactory{

    public Loan sampleLoan (){
        return Loan.builder()
                .amount(new BigDecimal("10000"))
                .totalAmount(new BigDecimal("10500"))
                .interestRate(new BigDecimal("40"))
                .initialSubscriptionRate(new BigDecimal("40"))
                .currentSubscriptionRate(new BigDecimal("40"))
                .startDate(new Date())
                .initialFinishDate(new Date())
                .currentFinishDate(new Date())
                .paidAmount(new BigDecimal("500"))
                .unpaidAmount(new BigDecimal("10000"))
                .pendingAmount(new BigDecimal("10000"))
                .paidSubscriptions(0)
                .unpaidSubscription(12)
                .status(LoanStatus.UNCOMPLETED)
                .creationStatus(LoanCreationStatus.CHOSE_LOAN_TYPE)
                .nextPayment(new Date())
                .interestType(LoanInterestType.FIXED)
                .subscriptionPeriodicity(LoanSubscriptionPeriodicity.MONTHLY)
                .amortizeInterest(new BigDecimal("100"))
                .loanType(LoanType.MORTGAGE)
                .loanNumberPayments(12)
                .build();
    }

    public Loan sampleCustomLoan(int loanNumberPayments, BigDecimal amount, BigDecimal interestRate){
        return Loan.builder()
                .loanNumberPayments(loanNumberPayments)
                .amount(amount)
                .interestRate(interestRate)
                .build();
    }


}
