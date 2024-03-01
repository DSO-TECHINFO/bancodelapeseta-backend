package com.banco.controllers;

import com.banco.entities.Loan;
import com.banco.entities.LoanPayment;
import com.banco.services.CalculateAmortizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/test")
public class TestController {

    CalculateAmortizationService calculateAmortizationService;

    @PostMapping("/amortization")
    public ResponseEntity<List<LoanPayment>> testAmortization (@RequestBody Loan loan) {
        return ResponseEntity.ok(calculateAmortizationService.calculateAmortization(loan));
    }
}
