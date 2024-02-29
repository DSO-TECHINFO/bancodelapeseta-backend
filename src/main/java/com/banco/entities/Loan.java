package com.banco.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private BigDecimal amount; // 10,2

    private BigDecimal interestRate; // 10,7

    private LoanSubscriptionPeriodicity loanSubscriptionPeriodicity;

    private int loanNumberPayments;


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


    public int getLoanNumberPayments() {
        return loanNumberPayments;
    }

    public void setLoanNumberPayments(int loanNumberPayments) {
        this.loanNumberPayments = loanNumberPayments;
    }
}
