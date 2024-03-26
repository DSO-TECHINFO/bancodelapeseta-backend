package com.banco.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.banco.utils.AccountUtils;
import com.banco.utils.CopyNonNullFields;
import com.banco.utils.EntityUtils;
import com.banco.dtos.CardCreateDto;
import com.banco.dtos.CardCredentialsDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.Account;
import com.banco.entities.Card;
import com.banco.entities.Contract;
import com.banco.entities.ContractType;
import com.banco.entities.Entity;
import com.banco.entities.EntityContract;
import com.banco.entities.EntityContractRole;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityContractRepository;
import com.banco.security.AesService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private final EntityUtils entityUtils;
    private final CopyNonNullFields copyNonNullFields;
    private final VerifyService verifyService;
    private final AesService aesService;
    private final EntityContractRepository entityContractRepository;

    private Card getCard(String cardNumber) {
        List<EntityContract> contracts = getUserCards();
        return contracts.stream().map(EntityContract::getContract).map(Contract::getCard).filter(card -> card.getNumber().equals(cardNumber)).findFirst().orElseThrow(() -> new CustomException("CARD-001", "Card not found", 404));
    }
    
    @Override
    public List<EntityContract> getUserCards() throws CustomException {
        return entityUtils.checkIfEntityExists(entityUtils.extractUser())
                .getContracts().stream().filter(contract -> contract.getContract().getType() == ContractType.CARD).collect(Collectors.toList());
    }

    @Override
    public CardCredentialsDto getCredentials(String cardNumber, VerificationCodeDto verificationCodeDto) throws CustomException {
        verifyService.verifyTransactionCode(verificationCodeDto.getVerificationCode(), true);
        Card card = this.getCard(cardNumber);
        try {
            card.setCvv(aesService.decrypt(card.getCvv()));
            card.setPin(aesService.decrypt(card.getPin()));
            CardCredentialsDto cardCredentialsDto = new CardCredentialsDto();
            copyNonNullFields.copyNonNullProperties(card, cardCredentialsDto, false);
            return cardCredentialsDto;
        } catch(CustomException exception) {
            throw new CustomException("CARD-030", "Bad request.\n" + exception.getMessage(), 400);
        } catch(Exception exception) {
            throw new CustomException("CARD-050", "Unexpected error, please contact support: " + exception.getMessage(), 500);
        }
    }

    @Override
    public Card createCard(CardCreateDto cardCreateDto) throws CustomException {
        verifyService.verifyTransactionCode(cardCreateDto.getVerificationCode(), true);
        
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());

        List<EntityContractRole> allowedRoles = new ArrayList<>();
        allowedRoles.add(EntityContractRole.OWNER);
        allowedRoles.add(EntityContractRole.CO_OWNER);
        allowedRoles.add(EntityContractRole.AUTHORIZED);

        CustomException userNotAllowed = new CustomException("CARD-002", "Overseers are not allowed to create cards", 403);
        Account account = AccountUtils.getAccountIfUserContractRoleIsAllowed(user, cardCreateDto.getAccountNumber(), allowedRoles, userNotAllowed);


        // Contract contract = Contract.builder().account(account).card(null);
        // EntityContract entityContract = EntityContract.builder().entity(user).contract(null);

        // entityContractRepository.save(null);
        return null;
    }
}
