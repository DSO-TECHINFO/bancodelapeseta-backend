package com.banco;

import com.banco.dtos.CreateNewAccountDto;
import com.banco.dtos.PasswordChangeDto;
import com.banco.dtos.PhoneChangeDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.*;
import com.banco.repositories.EntityRepository;
import com.banco.repositories.TransferRepository;
import com.banco.security.JwtService;
import com.banco.services.VerifyService;
import com.banco.utils.EntityUtils;
import jakarta.transaction.Transactional;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AccountControllerTests {

    @MockBean
    private EntityRepository entityRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @MockBean
    private TransferRepository transferRepository;

    @Test
    @WithMockUser
    @Transactional
    public void testGetAccountsTestIsOk() throws Exception {

        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .contracts(new ArrayList<>())
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/accounts").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(MockMvcResultMatchers.content().json("[]"))
                .andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    @Transactional
    public void testGetAccountsTestPhoneNotConfirmed() throws Exception {

        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .contracts(new ArrayList<>())
                        .emailConfirmed(true)
                        .phoneConfirmed(false)
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/accounts").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(status().isForbidden());
    }
    @Test
    @WithMockUser
    @Transactional
    public void testCreateAccountTestIsOk() throws Exception {
        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .id(18L)
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .verifyWithSign(true)
                        .type(EntityType.PHYSICAL)
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .post("/accounts/create").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(CreateNewAccountDto.builder().currency("EUR").productId(2L).verificationCode(mockedCode).build()))
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser
    @Transactional
    public void testCloseAccountTestIsOk() throws Exception {
        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .id(18L)
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .verifyWithSign(true)
                        .contracts(List.of(EntityContract.builder().contract(
                                Contract.builder().deactivated(false).type(ContractType.ACCOUNT).creationDate(new Date()).account(
                                        Account.builder().locked(false).accountNumber("TEST").build())
                                        .build())
                                .build()))
                        .type(EntityType.PHYSICAL)
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));
        when(transferRepository.findAllByPayerAccount(any()))
                .thenReturn(List.of(
                        Transfer.builder().status(TransferStatus.COMPLETED).build()
                ));


        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/accounts/close/TEST").contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(VerificationCodeDto.builder().verificationCode(mockedCode).build()))
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(status().isNoContent());
    }
}
