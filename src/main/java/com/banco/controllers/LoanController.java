package com.banco.controllers;

import com.banco.dtos.LoanDto;
import com.banco.dtos.LoanRequestDto;
import com.banco.exceptions.CustomException;
import com.banco.services.LoanServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@AllArgsConstructor
public class LoanController {
    LoanServiceImpl loanService;


    @GetMapping
    public ResponseEntity<List<LoanDto>> getLoans() throws CustomException {
        return ResponseEntity.ok(loanService.getLoans());
    }

    @PostMapping("/create/{accountId}/{productId}")
    public ResponseEntity<?> createLoan(@PathVariable("accountId") Long accountId, @PathVariable("productId") Long productId, @RequestBody LoanRequestDto loanRequestDto){
        loanService.loanCreation(loanRequestDto, accountId, productId);
        return ResponseEntity.ok(200);
    }
}
