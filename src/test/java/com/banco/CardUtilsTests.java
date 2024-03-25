package com.banco;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.banco.entities.CardNetwork;
import com.banco.entities.CardType;
import com.banco.exceptions.CustomException;
import com.banco.utils.CardUtils;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class CardUtilsTests {

    @Autowired
    CardUtils cardUtils;

    @Test
    void testLuhnAlgorithmOk() {
        Assertions.assertDoesNotThrow(() -> cardUtils.isCardNumberValid("4379312147668283"));
    }

    @Test
    void testLuhnAlgorithmFail() {
        Assertions.assertThrows(CustomException.class, () -> cardUtils.isCardNumberValid("123456789012348"));
    }

    @Test
    void testGenerateCardNumber() {
        CardNetwork cardNetwork = CardNetwork.VISA;
        CardType cardType = CardType.DEBIT;

        String account = "ES197912001098";
        String name = "Jesus Martinez Garcia";
        int nthAccountCard = 1;
        String expectedCardNumber = "4500 8148 8431 367";

        String card = cardUtils.createCardNumber(cardNetwork, cardType, account, name, nthAccountCard);
        Assertions.assertEquals(expectedCardNumber, card);
        Assertions.assertDoesNotThrow(() -> cardUtils.isCardNumberValid(expectedCardNumber));
    }
}
