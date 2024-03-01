package com.banco.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
public class LoanPayment {

        private int paymentNumber;

        private BigDecimal paymentAmount;

        private BigDecimal InterestPaid;

        private BigDecimal principalPaid;

        private BigDecimal remainingBalance;
    private Long id;

    public LoanPayment() {

        }


    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setPaymentNumber(int paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        InterestPaid = interestPaid;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    @Override
    public String toString() {
        return "LoanPayment{" +
                "paymentNumber=" + paymentNumber +
                ", paymentAmount=" + paymentAmount +
                ", InterestPaid=" + InterestPaid +
                ", principalPaid=" + principalPaid +
                ", remainingBalance=" + remainingBalance +
                '}';
    }
}
