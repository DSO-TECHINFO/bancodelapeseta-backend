package com.banco.dtos;

import com.banco.entities.TransferStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
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
public class CreateTransferDto {

    private String beneficiaryName;
    private String destinationAccount;
    private String payerName;
    private String payerAccount;
    private String concept;
    private String description;
    private BigDecimal amount;
    private String currency;
    private String verificationCode;
}
