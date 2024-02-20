package com.banco;

import com.banco.dtos.AuthenticationRequestDto;
import com.banco.dtos.EmailPhoneVerificationDto;
import com.banco.dtos.TransactionVerificationDto;
import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;
import com.banco.security.JwtService;
import com.banco.services.VerifyServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class VerificationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EntityRepository entityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private VerifyServiceImpl verifyService;
    @Test
    @WithMockUser
    @Transactional
    public void testVerifyEmailCode() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .emailConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                                .emailConfirmationCodeAttempts(0)
                                .emailConfirmationCode(passwordEncoder.encode(mockedCode))
                        .build()));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/verify/email").contentType(MediaType.APPLICATION_JSON).header("Authorization", jwtService.generateToken(new Entity())).content(TestUtils.asJsonString(EmailPhoneVerificationDto.builder().code(mockedCode).build())))
                .andExpect(status().isOk());


    }
    @Test
    @WithMockUser
    @Transactional
    public void testVerifyPhoneCode() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .phoneConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .phoneConfirmationCodeAttempts(0)
                        .phoneConfirmationCode(passwordEncoder.encode(mockedCode))
                        .build()));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/verify/phone").contentType(MediaType.APPLICATION_JSON).header("Authorization", jwtService.generateToken(new Entity())).content(TestUtils.asJsonString(EmailPhoneVerificationDto.builder().code(mockedCode).build())))
                .andExpect(status().isOk());


    }
    @Test
    @WithMockUser
    @Transactional
    public void testVerifyUnsignedCodeOk() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .emailConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .emailConfirmed(true)
                        .emailConfirmationCodeAttempts(0)
                        .emailConfirmationCode(passwordEncoder.encode(mockedCode))
                        .phoneConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .phoneConfirmed(true)
                        .phoneConfirmationCodeAttempts(0)
                        .phoneConfirmationCode(passwordEncoder.encode(mockedCode))
                        .build()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/verify/transaction/unsigned").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtService.generateToken(new Entity()))
                        .content(TestUtils.asJsonString(TransactionVerificationDto.builder().emailCode(mockedCode).phoneCode(mockedCode).build())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("verificationCode"));

    }
    @Test
    @WithMockUser
    @Transactional
    public void testVerifySignedCodeOk() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .emailConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .emailConfirmed(true)
                        .emailConfirmationCodeAttempts(0)
                        .emailConfirmationCode(passwordEncoder.encode(mockedCode))
                        .phoneConfirmationCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .phoneConfirmed(true)
                        .phoneConfirmationCodeAttempts(0)
                        .phoneConfirmationCode(passwordEncoder.encode(mockedCode))
                        .sign(passwordEncoder.encode(mockedCode))
                        .signActivated(true)
                        .build()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .post("/verify/transaction/unsigned").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", jwtService.generateToken(new Entity()))
                        .content(TestUtils.asJsonString(TransactionVerificationDto.builder().emailCode(mockedCode).phoneCode(mockedCode).sign(mockedCode).build())))
                .andExpect(status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.contains("verificationCode"));

    }
    @Test
    @WithMockUser
    @Transactional
    public void testTransactionVerifyUnsignedOk() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                                .verifyTransactionCodeAttempts(0)
                                .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));

        Assertions.assertTrue(verifyService.verifyTransactionCode(mockedCode, false));

    }
    @Test
    @WithMockUser
    @Transactional
    public void testTransactionVerifySignedOk() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                                .verifyWithSign(true)
                        .build()));

        Assertions.assertTrue(verifyService.verifyTransactionCode(mockedCode, true));

    }
    @Test
    @WithMockUser
    @Transactional
    public void testTransactionVerifySignedFail() throws Exception {

        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .verifyWithSign(false)
                        .build()));

        Assertions.assertThrows(CustomException.class,() -> verifyService.verifyTransactionCode(mockedCode, true));

    }

}
