package com.banco.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AccountDto account;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal amount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal totalAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal interestRate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal initialSubscriptionRate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal currentSubscriptionRate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date startDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date initialFinishDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date currentFinishDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal paidAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal unpaidAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal pendingAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Integer paidSubscriptions;
}
