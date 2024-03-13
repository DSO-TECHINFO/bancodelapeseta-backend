package com.banco.controllers;

import com.banco.dtos.LoanDto;
import com.banco.dtos.LoanRequestDto;
import com.banco.exceptions.CustomException;
import com.banco.services.LoanServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping("/create")
    public ResponseEntity<?> createLoan(@RequestBody @Validated LoanRequestDto loanRequestDto){
        loanService.loanCreation(loanRequestDto);
        return ResponseEntity.ok(200);
    }
}
