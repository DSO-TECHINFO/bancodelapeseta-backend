package com.banco.services;

import com.banco.dtos.CurrencyExchangeDto;

public interface CurrencyExchangeService {
    CurrencyExchangeDto getCurrencyExchangeRate(String currencyFrom);
}
