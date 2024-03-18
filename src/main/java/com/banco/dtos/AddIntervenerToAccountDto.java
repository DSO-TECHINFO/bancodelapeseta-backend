package com.banco.dtos;

import com.banco.entities.EntityContractRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddIntervenerToAccountDto {
    private String taxId;
    private String accountNumber;
    private EntityContractRole role;
    private String verificationCode;
}
