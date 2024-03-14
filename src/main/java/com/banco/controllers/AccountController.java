package com.banco.controllers;

import com.banco.dtos.AddIntervenerToAccountDto;
import com.banco.dtos.CreateNewAccountDto;
import com.banco.dtos.RemoveIntervenerFromAccountDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.exceptions.CustomException;
import com.banco.services.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/accounts")
public class AccountController {

    private final AccountService accountService;
    @GetMapping
    public ResponseEntity<?> getAccounts() throws CustomException {
        return ResponseEntity.ok(accountService.getAccounts());
    }
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateNewAccountDto createNewAccountDto) throws CustomException {
        accountService.createAccount(createNewAccountDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/close/{accountNumber}")
    public ResponseEntity<?> deactivateAccount(@PathVariable String accountNumber, @RequestBody VerificationCodeDto verificationCodeDto) throws CustomException {
        accountService.deactivateAccount(accountNumber, verificationCodeDto);
        return ResponseEntity.noContent().build();

    }
    @PostMapping("/add/intervener")
    public ResponseEntity<?> addIntervenerToAccount(@RequestBody AddIntervenerToAccountDto addIntervenerToAccountDto){
        accountService.addIntervenerToAccount(addIntervenerToAccountDto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/remove/intervener")
    public ResponseEntity<?> removeIntervenerFromAccount(@RequestBody RemoveIntervenerFromAccountDto removeIntervenerFromAccountDto){
        accountService.removeIntervenerFromAccount(removeIntervenerFromAccountDto);
        return ResponseEntity.ok().build();
    }
}
