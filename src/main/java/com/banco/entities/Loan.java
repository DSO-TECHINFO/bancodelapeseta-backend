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

@Entity
@Table(name = "loan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "loan")
    private Contract contract;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToOne
    private Account account;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal amount;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal totalAmount;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal interestRate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal initialSubscriptionRate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal currentSubscriptionRate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date startDate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date initialFinishDate;

    @JsonProperty (access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date currentFinishDate;

    @JsonProperty(access =  JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal paidAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal unpaidAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal pendingAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Integer paidSubscriptions;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Integer unpaidSubscription;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private LoanStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private LoanCreationStatus creationStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date nextPayment;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private LoanInterestType interestType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private LoanSubscriptionPeriodicity subscriptionPeriodicity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal amortizeInterest;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private LoanType loanType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Integer loanNumberPayments;
}