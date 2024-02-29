package com.banco.controllers;

import com.banco.entities.Loan;
import com.banco.entities.LoanPayment;
import com.banco.services.CalculateAmortizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("test/")
public class TestController {

    CalculateAmortizationService calculateAmortizationService;
    Loan loan;

    @GetMapping("amortization")
    public List<LoanPayment> testAmortization(Loan loan){
        return calculateAmortizationService.calculateAmortization(loan);
    }
}
