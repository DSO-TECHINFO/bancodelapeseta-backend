package com.banco.controllers;

import com.banco.dtos.CardCredentialsDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;
import com.banco.services.CardService;
import com.banco.services.VerifyService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/card")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<List<EntityContract>> getAllCards() throws CustomException {
        List<EntityContract> cardDtoList = cardService.getUserCards();
        return ResponseEntity.ok(cardDtoList);
    }

    @GetMapping("/credentials/{cardNumber}")
    public ResponseEntity<CardCredentialsDto> getCardCredentials(@PathVariable String cardNumber, @RequestBody VerificationCodeDto verificationCodeDto) throws CustomException {
        CardCredentialsDto cardCredentialsDto = cardService.getCredentials(cardNumber, verificationCodeDto);
        return ResponseEntity.ok(cardCredentialsDto);
    }
    
}
