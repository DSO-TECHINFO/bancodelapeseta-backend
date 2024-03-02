package com.banco.controllers;

import com.banco.exceptions.CustomException;
import com.banco.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    private final AccountService accountService;
    @GetMapping
    public ResponseEntity<?> getAccounts() throws CustomException {
        return ResponseEntity.ok(accountService.getAccounts());
    }
}
