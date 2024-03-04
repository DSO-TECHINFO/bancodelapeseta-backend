package com.banco.services;

import java.util.List;

import org.springframework.stereotype.Service;


import com.banco.utils.EntityUtils;
import com.banco.dtos.CardDto;
import com.banco.entities.Card;
import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.repositories.CardRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;
    private final EntityUtils entityUtils;
    
    @Override
    public List<CardDto> getUserCards() throws CustomException {
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        List<Card> cardList = cardRepository.findAllCardsFromEntityTaxId(user.getTaxId());
        return CardService.cardListToCardDtoList(cardList);
    }
}
