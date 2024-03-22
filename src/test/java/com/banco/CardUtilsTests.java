package com.banco;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
