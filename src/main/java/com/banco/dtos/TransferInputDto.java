package com.banco.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferInputDto {
    public String originAccount;
    public String payerName;
    public String destinationAccount;
    public String beneficiaryName;
    public BigDecimal amount;
    public String concept;
    public String description;
}
