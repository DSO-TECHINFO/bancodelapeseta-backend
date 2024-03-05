package com.banco.services;

import com.banco.dtos.CurrencyExchangeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService{
    @Value("${currency.exchange.url}")
    private String EXCHANGE_URL;
    @Value("${currency.exchange.api-key}")
    private String API_KEY;
    @Override
    public BigDecimal getCurrencyExchangeRate(String currencyFrom, String currencyTo) {
        RestTemplate restTemplate = new RestTemplate();
        CurrencyExchangeDto access = restTemplate.getForObject(EXCHANGE_URL + "?access_key=" + API_KEY + "&base=" + currencyFrom, CurrencyExchangeDto.class);
        assert access != null;
        return access.getRates().get(currencyTo);
    }
}
