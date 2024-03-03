package com.banco.controllers;

import com.banco.dtos.CardDto;
import com.banco.dtos.TransactionVerificationDto;
import com.banco.entities.Card;
import com.banco.exceptions.CustomException;
import com.banco.services.CardService;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/card")
public class CardController {

    private final CardService cardService;

    @GetMapping("/")
    public ResponseEntity<List<CardDto>> getAllCards() throws CustomException {
        List<CardDto> cardDtoList = cardService.getUserCards();
        return ResponseEntity.ok(cardDtoList);
    }

    @PostMapping("/access/sensitiveinfo")
    public ResponseEntity<?> accessSensitiveInfo(@RequestBody TransactionVerificationDto transactionVerificationDto)
            throws CustomException {
        cardService.accessSensitiveInfo(transactionVerificationDto);
        return ResponseEntity.ok().build();
    }
}
