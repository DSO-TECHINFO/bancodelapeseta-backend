package com.banco.services;

import com.banco.entities.Currency;
import com.banco.repositories.CurrencyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyServiceImpl implements CurrencyService{
    private CurrencyRepository currencyRepository;
    @Override
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }
}
