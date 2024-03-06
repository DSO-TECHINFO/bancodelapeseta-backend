package com.banco.services;

import com.banco.dtos.CurrencyExchangeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService{
    @Value("${currency.exchange.url}")
    private String EXCHANGE_URL;
    @Value("${currency.exchange.api-key}")
    private String API_KEY;
    @Override
    public CurrencyExchangeDto getCurrencyExchangeRate(String currencyFrom) {
        RestTemplate restTemplate = new RestTemplate();
        CurrencyExchangeDto access = restTemplate.getForObject(EXCHANGE_URL + "?access_key=" + API_KEY + "&base=" + currencyFrom, CurrencyExchangeDto.class);
        assert access != null;
        return access;
    }
}
