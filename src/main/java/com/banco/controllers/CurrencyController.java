package com.banco.controllers;

import com.banco.services.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@RequestMapping(path = "/currency")
public class CurrencyController {
    private CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<?> getAllCurrencies(){
        return ResponseEntity.ok(currencyService.getAllCurrencies());
    }
}
