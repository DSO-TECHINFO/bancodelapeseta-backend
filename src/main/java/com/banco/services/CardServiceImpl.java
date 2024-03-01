package com.banco.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.repositories.CardRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

}
