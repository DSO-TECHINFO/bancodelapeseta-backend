package com.banco.controllers;

import com.banco.dtos.TransactionVerificationDto;
import com.banco.exceptions.CustomException;
import com.banco.services.CardService;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/card")
public class CardController {

    private final CardService cardService;

    @PostMapping("/access/sensitiveinfo/")
    public ResponseEntity<?> accessSensitiveInfo(@RequestBody TransactionVerificationDto transactionVerificationDto)
            throws CustomException {
        cardService.accessSensitiveInfo(transactionVerificationDto);
        return ResponseEntity.ok().build();
    }
}
