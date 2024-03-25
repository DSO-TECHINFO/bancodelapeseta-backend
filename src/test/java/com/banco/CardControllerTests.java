package com.banco;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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

import com.banco.dtos.CardCredentialsDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.Card;
import com.banco.entities.Contract;
import com.banco.entities.ContractType;
import com.banco.entities.Entity;
import com.banco.entities.EntityContract;
import com.banco.repositories.EntityRepository;
import com.banco.security.AesService;
import com.banco.security.JwtService;
import com.banco.utils.CopyNonNullFields;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CardControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @MockBean
    private EntityRepository entityRepository;
    @Autowired
    private CopyNonNullFields copyNonNullFields;
    @Autowired 
    PasswordEncoder passwordEncoder;
    @Autowired
    AesService aesService;
    
    @Test
    @WithMockUser
    void testCardListEmptyOk() throws Exception {
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .contracts(new ArrayList<>())
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .build()));
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/card")
                    .header("Authorization","Bearer " + jwtService.generateToken(new Entity()))) 
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testCardListOk() throws Exception {
        List<EntityContract> mockEntityContracts = new ArrayList<>();
        List<Contract> mockContracts = new ArrayList<>();
        List<Card> mockCards = new ArrayList<>();
        mockCards.add(Card.builder().number("1234 5678 9012").build());
        mockCards.add(Card.builder().number("2345 6789 0123").build());
        mockCards.add(Card.builder().number("1234 5678 9012").build());
        mockCards.add(Card.builder().number("3456 7890 1234").build());
        mockCards.forEach(card -> {
            Contract mockContract = Contract.builder().card(card).type(ContractType.CARD).build();
            EntityContract mockEntityContract = EntityContract.builder().contract(mockContract).build();
            mockContracts.add(mockContract);
            mockEntityContracts.add(mockEntityContract);
        });

        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                .contracts(mockEntityContracts)
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .build()));
        

        mockMvc.perform(MockMvcRequestBuilders
                    .get("/card")
                    .header("Authorization","Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(MockMvcResultMatchers.content().json(TestUtils.asJsonString(mockEntityContracts)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    @WithMockUser
    void testCardCredentialsOk() throws Exception {
        String mockedCode = "CODETEST";
        String cvv = "123";
        String pin = "1234";
        Card mockCardRequest = Card.builder().number("1234 5678 9012").cvv(aesService.encrypt(cvv)).pin(aesService.encrypt(pin)).build();
        List<EntityContract> mockEntityContracts = new ArrayList<>();
        Contract mockContract = Contract.builder().card(mockCardRequest).type(ContractType.CARD).build();
        mockEntityContracts.add(EntityContract.builder().contract(mockContract).build());
        CardCredentialsDto mockCardCredentialsDto = new CardCredentialsDto();
        Card mockCardResponse = Card.builder().number("1234 5678 9012").cvv(cvv).pin(pin).build();
        copyNonNullFields.copyNonNullProperties(mockCardResponse, mockCardCredentialsDto, false);

        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .contracts(mockEntityContracts)
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .verifyWithSign(true)
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));

        mockMvc.perform(MockMvcRequestBuilders
                    .get("/card/credentials/1234 5678 9012")
                    .content(TestUtils.asJsonString(VerificationCodeDto.builder().verificationCode(mockedCode).build())).contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization","Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(MockMvcResultMatchers.content().json(TestUtils.asJsonString(mockCardCredentialsDto)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    @WithMockUser
    void testCardCredentialsNotFoundFail() throws Exception {
        String mockedCode = "CODETEST";
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .contracts(new ArrayList<>())
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .verifyWithSign(true)
                        .verifyTransactionCode(passwordEncoder.encode(mockedCode))
                        .verifyTransactionCodeAttempts(0)
                        .verifyTransactionCodeExpiration(new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10)))
                        .build()));

        mockMvc.perform(MockMvcRequestBuilders
                    .get("/card/credentials/1234 5678 9012")
                    .content(TestUtils.asJsonString(VerificationCodeDto.builder().verificationCode(mockedCode).build())).contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization","Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

}
