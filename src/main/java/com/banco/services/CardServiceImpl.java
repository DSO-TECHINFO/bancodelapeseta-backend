package com.banco.services;

import com.banco.entities.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

import com.banco.repositories.EntityRepository;
import com.banco.utils.EntityUtils;
import com.banco.dtos.CardDto;
import com.banco.dtos.TransactionVerificationDto;
import com.banco.exceptions.CustomException;
import com.banco.repositories.CardRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityRepository entityRepository;
    private final EntityUtils entityUtils;
    private final VerifyService verifyService;

    @Override
    public void accessSensitiveInfo(TransactionVerificationDto transactionVerificationDto) throws CustomException {
        Entity entity = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        if (verifyService.verifyTransactionCode(transactionVerificationDto.getSign(), true)) {
            if (entity.getVerifyTransactionCodeAttempts() > 3)
                throw new CustomException("USERS-032", "Too many attempts", 400);
            if (entity.getVerifyTransactionCodeExpiration().before(new Date()))
                throw new CustomException("USERS-033", "Code has expired", 400);
            if (!passwordEncoder.matches(transactionVerificationDto.getEmailCode(), entity.getEmailConfirmationCode()))
                throw new CustomException("USERS-034", "Email code is invalid", 400);
            if (!passwordEncoder.matches(transactionVerificationDto.getPhoneCode(), entity.getPhoneConfirmationCode()))
                throw new CustomException("USERS-035", "Phone code is invalid", 400);
            entity.setVerifyWithSign(true);
            entity.setVerifyTransactionCodeAttempts(0);
            entity.setVerifyTransactionCodeExpiration(null);
            System.out.println("Sensitive info accessed");
            entityRepository.save(entity);
            // RETURN CARD INFO

        }
    }
    
    @Override
    public List<CardDto> getUserCards() throws CustomException {
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        List<Card> cardList = cardRepository.findAllCardsFromEntityTaxId(user.getTaxId());
        List<CardDto> cardDtoList = new ArrayList<>();
        for(Card card : cardList) {
            CardDto cardDto = CardDto.builder()
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
            cardDtoList.add(cardDto);
        }
        return cardDtoList;
    }
}
