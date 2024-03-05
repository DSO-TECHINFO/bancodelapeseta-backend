package com.banco.controllers;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.dtos.ListingLoanDto;
import com.banco.exceptions.CustomException;
import com.banco.services.LoanService;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/loan")
public class LoanController {

    private final LoanService loanService;

    @GetMapping
    public ResponseEntity<List<ListingLoanDto>> getUserLoans() throws CustomException {
        List<ListingLoanDto> loanToDto = loanService.getUserLoans();
        return ResponseEntity.ok(loanToDto);
    }

}
