package com.banco.dtos;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListingLoanDto {
    private String contractId;
    private String amount;
    private String totalAmount;
    private String interestRate;
}
