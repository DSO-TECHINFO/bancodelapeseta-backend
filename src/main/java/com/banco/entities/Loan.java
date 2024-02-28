package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Prestamos")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Contract contract;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToOne
    private Account account;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    private BigDecimal amount;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalAmount;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    private BigDecimal interestRate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    private BigDecimal initialSubscriptionRate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    private BigDecimal currentSubscriptionRate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    private Date startDate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    private Date initialFinishDate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    private Date currentFinishDate;

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY)
    private BigDecimal paidAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal unpaidAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal pendingAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer paidSubscriptions;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer unpaidSubscription;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LoanStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LoanCreationStatus creationStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date nextPayment;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LoanInteresType interestType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LoanSubscriptionPeriodicity subscriptionPeriodicity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal amortizeInterest;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LoanType loanType;


}
