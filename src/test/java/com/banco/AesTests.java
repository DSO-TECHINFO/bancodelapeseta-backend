package com.banco;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.banco.security.AesService;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AesTests {

    @Autowired
    private AesService aesService;

    @Test
    void testAesEncryptOk() throws Exception {
        String text = "PLAIN CODE TEST";
        String expectedEncrypted = "yj+zNIgY2P/oRRyMppU+Yg==";
        String encryptedText = aesService.encrypt(text);
        Assertions.assertEquals(expectedEncrypted, encryptedText);
    }

    @Test
    void testAesDecryptOk() throws Exception {
        String encryptedText = "yj+zNIgY2P/oRRyMppU+Yg==";
        String expectedPlainText = "PLAIN CODE TEST";
        String decryptedText = aesService.decrypt(encryptedText);
        Assertions.assertEquals(expectedPlainText, decryptedText);
    }
}
