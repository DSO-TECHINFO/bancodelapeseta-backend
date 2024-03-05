package com.banco.entities;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table( name = "amortizationPlan")
public class AmortizationPlan {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private Long id;

    @Column
    @DateTimeFormat
    private Date date;

    @Column
    private BigDecimal rate;

    @Column
    private BigDecimal amortCap;

    @Column
    private BigDecimal interests;

    @Column
    private BigDecimal pendingCapital;

    @Column
    @OneToOne
    private Loan loan;

}
