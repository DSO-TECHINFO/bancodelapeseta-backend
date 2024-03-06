package com.banco.controllers;

import com.banco.services.CurrencyExchangeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/exchange")
public class CurrencyExchangeController {

    private CurrencyExchangeService currencyExchangeService;

    @GetMapping("")
    public ResponseEntity<?> getCurrencyExchange(@RequestParam String currencyFrom){
        return  ResponseEntity.ok(currencyExchangeService.getCurrencyExchangeRate(currencyFrom));
    }
}
