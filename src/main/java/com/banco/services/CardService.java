package com.banco.services;

import java.util.ArrayList;
import java.util.List;

import com.banco.dtos.CardDto;
import com.banco.entities.Card;
import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;

public interface CardService {
    List<EntityContract> getUserCards() throws CustomException; 

    static List<CardDto> cardListToCardDtoList(List<Card> cardList) {
        List<CardDto> cardDtoList = new ArrayList<>();
        for(Card card : cardList) {
            cardDtoList.add(cardToCardDto(card));
        }
        return cardDtoList;
    }

    static CardDto cardToCardDto(Card card) {
        return CardDto.builder()
                .id(card.getId())
                .number(card.getNumber())
                .expiration(card.getExpiration())
                .cashierLimit(card.getCashierLimit())
                .dailyBuyoutLimit(card.getDailyBuyoutLimit())
                .activated(card.getActivated())
                .activationDate(card.getActivationDate())
                .cardType(card.getCardType())
                .chargedAmount(card.getChargedAmount())
                .fee(card.getFee())
                .build();
    }
}
