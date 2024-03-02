package com.banco.entities;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int paymentNumber;

    @Column
    private BigDecimal paymentAmount;

    @Column
    private BigDecimal interestPaid;
    @Column
    private BigDecimal amortization;
    @Column
    private BigDecimal remainingBalance;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanPayment that = (LoanPayment) o;
        return paymentNumber == that.paymentNumber && Objects.equals(id, that.id) && Objects.equals(paymentAmount, that.paymentAmount) && Objects.equals(interestPaid, that.interestPaid) && Objects.equals(amortization, that.amortization) && Objects.equals(remainingBalance, that.remainingBalance);
    }
}
