package com.banco.services;

import java.math.BigDecimal;

public interface CurrencyExchangeService {
    BigDecimal getCurrencyExchangeRate(String currencyFrom, String currencyTo);
}
