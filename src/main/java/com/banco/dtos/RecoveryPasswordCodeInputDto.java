package com.banco.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecoveryPasswordCodeInputDto {
    private String taxId;
    private String emailCode;
    private String phoneCode;
    private String sign;
}
