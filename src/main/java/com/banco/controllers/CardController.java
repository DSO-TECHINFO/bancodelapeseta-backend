package com.banco.controllers;

import com.banco.dtos.CardCredentialsDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;
import com.banco.services.CardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/card")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<List<EntityContract>> getAllCards() throws CustomException {
        List<EntityContract> cardDtoList = cardService.getUserCards();
        return ResponseEntity.ok(cardDtoList);
    }

    @GetMapping("/credentials/{cardNumber}")
    public ResponseEntity<CardCredentialsDto> getCardCredentials(@PathVariable String cardNumber, @RequestBody VerificationCodeDto verificationCodeDto) throws CustomException {
        CardCredentialsDto cardCredentialsDto = cardService.getCredentials(cardNumber, verificationCodeDto);
        return ResponseEntity.ok(cardCredentialsDto);
    }

    /**
     * Deletes a card by deactivating the associated account.
     *
     * @param cardNumber The number card to deactivate.
     * @return ResponseEntity representing the result of the deactivation process.
     * @throws CustomException if there is an error during the deactivation process.
     */
    @DeleteMapping("/close/{cardNumber}")
    public ResponseEntity<?> deactivateCard(@PathVariable String cardNumber) throws CustomException {
        // Call the account service to deactivate the account associated with the card
        cardService.deactivateCard(cardNumber);

        // Return a ResponseEntity with no content to indicate successful deactivation
        return ResponseEntity.noContent().build();
    }
    
}
