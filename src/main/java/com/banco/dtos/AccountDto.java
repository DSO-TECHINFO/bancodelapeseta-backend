package com.banco.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class AccountDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
   private BigDecimal balance;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal real_balance;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CurrencyDto currency;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String account_number;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean locked;


}
