package com.banco.dtos;

import com.banco.entities.LoanInterestType;
import com.banco.entities.LoanSubscriptionPeriodicity;
import com.banco.entities.LoanType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.annotations.NotNull;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequestDto {


    @NotNull
    private BigDecimal amount;

    @NotNull
    private BigDecimal interestRate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date initialFinishDate;

    @NotNull
    private LoanInterestType interestType;

    @NotNull
    private LoanSubscriptionPeriodicity subscriptionPeriodicity;

    @NotNull
    private LoanType loanType;

    @NotNull
    private Integer loanNumberPayments;

    @NotNull
    private Long productId;

  }
