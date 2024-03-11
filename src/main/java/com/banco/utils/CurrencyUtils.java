package com.banco.utils;

import com.banco.entities.Currency;
import com.banco.exceptions.CustomException;
import com.banco.repositories.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class CurrencyUtils {
    private final CurrencyRepository currencyRepository;

    public Optional<Currency> extractCurrency(String currency){
        return currencyRepository.findByCurrency(currency);
    }
    public Currency checkCurrency(Optional<Currency> currency) throws CustomException {
        return currency.orElseThrow(()->new CustomException("CURRENCY-001", "Currency not found", 404));
    }
}
