package com.banco.controllers;

import com.banco.entities.Loan;
import com.banco.services.LoanServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/loans")
@AllArgsConstructor
public class LoanController {
    LoanServiceImpl loanService;

    @GetMapping
    public ResponseEntity<List<Loan>> getLoans(){
        return ResponseEntity.ok(loanService.getLoans());
    }
}
