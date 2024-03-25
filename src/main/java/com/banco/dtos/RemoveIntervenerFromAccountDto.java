package com.banco.dtos;

import com.banco.entities.EntityContractRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RemoveIntervenerFromAccountDto {

    private String accountNumber;
    private String taxId;
    private String verificationCode;
}
