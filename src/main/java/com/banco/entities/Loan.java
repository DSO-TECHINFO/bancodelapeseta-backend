package com.banco.entities;

import java.math.BigDecimal;

public class Loan {

    private String id;

    private BigDecimal amount; // 10,2

    private BigDecimal interestRate; // 10,7

    private LoanSubscriptionPeriodicity loanSubscriptionPeriodicity;

    private int loanMonthTerms;

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public LoanSubscriptionPeriodicity getLoanSubscriptionPeriodicity() {
        return loanSubscriptionPeriodicity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public void setLoanSubscriptionPeriodicity(LoanSubscriptionPeriodicity loanSubscriptionPeriodicity) {
        this.loanSubscriptionPeriodicity = loanSubscriptionPeriodicity;
    }

    public int getLoanMonthTerms() {
        return loanMonthTerms;
    }

    public void setLoanMonthTerms(int loanMonthTerms){
        this.loanMonthTerms = loanMonthTerms;
    }
}
