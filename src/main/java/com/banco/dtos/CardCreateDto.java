package com.banco.dtos;

import com.banco.entities.CardNetwork;
import com.banco.entities.CardType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardCreateDto {
    private CardType cardType;
    private CardNetwork cardNetwork;
    private String accountNumber;
    private String verificationCode;
}
