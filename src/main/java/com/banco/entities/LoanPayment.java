package com.banco.entities;

import java.math.BigDecimal;

public class LoanPayment {

    private int paymentNumber;

    private BigDecimal paymentAmount;

    private BigDecimal interestPaid;

    private BigDecimal principalPaid;

    private BigDecimal remainingBalance;

    public LoanPayment() {
    }

    public int getPaymentNumber() {
        return paymentNumber;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public BigDecimal getPrincipalPaid() {
        return principalPaid;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setPaymentNumber(int paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        this.interestPaid = interestPaid;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }
}
