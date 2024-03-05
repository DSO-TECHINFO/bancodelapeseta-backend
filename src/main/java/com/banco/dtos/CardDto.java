package com.banco.dtos;

import java.math.BigDecimal;
import java.sql.Date;

import com.banco.entities.CardType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {
    private Long id;
    private String number;
    private String expiration;
    private BigDecimal cashierLimit;
    private BigDecimal dailyBuyoutLimit;
    private Boolean activated;
    private Date activationDate;
    private CardType cardType;
    private BigDecimal chargedAmount;
    private BigDecimal fee;
}
