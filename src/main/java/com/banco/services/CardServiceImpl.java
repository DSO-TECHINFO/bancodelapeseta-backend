package com.banco.services;

import com.banco.dtos.CardCredentialsDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.*;
import com.banco.exceptions.CustomException;
import com.banco.repositories.CardRepository;
import com.banco.repositories.EntityContractRepository;
import com.banco.security.AesService;
import com.banco.utils.CopyNonNullFields;
import com.banco.utils.EntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;
    private final EntityUtils entityUtils;
    private final CopyNonNullFields copyNonNullFields;
    private final VerifyService verifyService;
    private final AesService aesService;
    private EntityContractRepository entityContractRepository;
    
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
    /**
     * Deactivates a card based on the provided DeactivateDto.
     *
     * @param cardNumber The cardNumber to deactivate.
     * @throws CustomException if there is an error during the deactivation process.
     */
    @Override
    public void deactivateCard(String cardNumber) throws CustomException {
        // Retrieve the entity associated with the current user
        Entity entity = entityUtils.checkIfEntityExists(entityUtils.extractUser());

        // Find the card using the unique number provided in the DTO
        Card card = cardRepository.findByNumber(cardNumber);

        entity.getContracts()
                .stream()
                .filter(entityContract -> ContractType.CARD.equals(entityContract.getContract().getType()))
                .forEach(entityContract -> {
                    Contract contract = entityContract.getContract();
                    //Card entityCard = contract.getCard();
                    if (card != null && contract.getCard() != null && contract.getCard().getId().equals(card.getId())) {
                        // Deactivate the card and its associated contract
                        contract.setDeactivated(true);
                        contract.getCard().setActivated(false);
                        cardRepository.saveAndFlush(card);
                        entityContract.setContract(contract);
                        entityContractRepository.save(entityContract);
                    }
                });
    }
}
