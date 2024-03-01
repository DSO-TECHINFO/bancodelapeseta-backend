package com.banco.controllers;

import com.banco.exceptions.CustomException;
import com.banco.services.CardService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/card")
public class CardController {

    private final CardService cardService;
    
}
