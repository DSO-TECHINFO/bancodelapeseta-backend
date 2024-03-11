package com.banco.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.banco.utils.CopyNonNullFields;
import com.banco.utils.EntityUtils;
import com.banco.dtos.CardCredentialsDto;
import com.banco.dtos.CardDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.Card;
import com.banco.entities.Contract;
import com.banco.entities.ContractType;
import com.banco.entities.Entity;
import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;
import com.banco.repositories.CardRepository;
import com.banco.security.AesService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;
    private final EntityUtils entityUtils;
    private final CopyNonNullFields copyNonNullFields;
    private final VerifyService verifyService;
    private final AesService aesService;
    
    @Override
    public List<EntityContract> getUserCards() throws CustomException {
        return entityUtils.checkIfEntityExists(entityUtils.extractUser())
                .getContracts().stream().filter(contract -> contract.getContract().getType() == ContractType.CARD).collect(Collectors.toList());
    }

    @Override
    public CardCredentialsDto getCredentials(String cardNumber, VerificationCodeDto verificationCodeDto) throws CustomException {
        verifyService.verifyTransactionCode(verificationCodeDto.getVerificationCode(), true);
        List<EntityContract> contracts = getUserCards();
        Optional<Card> cardOptional = contracts.stream().map(EntityContract::getContract).map(Contract::getCard).filter(card -> card.getNumber().equals(cardNumber)).findFirst();
        if(cardOptional.isEmpty()) 
            throw new CustomException("CARD-001", "Card not found", 404);
        Card card = cardOptional.get();
        try {
            card.setCvv(aesService.decrypt(card.getCvv()));
            card.setPin(aesService.decrypt(card.getPin()));
            CardCredentialsDto cardCredentialsDto = new CardCredentialsDto();
            copyNonNullFields.copyNonNullProperties(card, cardCredentialsDto, false);
            return cardCredentialsDto;
        } catch(Exception exception) {
            throw new CustomException("CARD-050", "Something went wrong, please contact support", 500);
        }
    }
}
