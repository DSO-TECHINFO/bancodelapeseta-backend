package com.banco.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.banco.utils.EntityUtils;
import com.banco.dtos.CardDto;
import com.banco.entities.Contract;
import com.banco.entities.Entity;
import com.banco.entities.EntityContract;
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
        return CardService.cardListToCardDtoList(user.getContracts().stream()
                .map(EntityContract::getContract)
                .map(Contract::getCard)
                .collect(Collectors.toList()));
    }
}
