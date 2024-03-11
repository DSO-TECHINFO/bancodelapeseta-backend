package com.banco.services;

import com.banco.entities.*;
import com.banco.exceptions.CustomException;
import com.banco.repositories.CardRepository;
import com.banco.repositories.EntityContractRepository;
import com.banco.utils.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private EntityUtils entityUtils;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private EntityContractRepository entityContractRepository;

    @InjectMocks
    private CardServiceImpl cardService;

    private Card card;

    private Card card2;

    private Contract contract1;
    private Contract contract2;
    private List<Contract> contracts;

    private EntityContract entityContract1;
    private EntityContract entityContract2;
    private List<EntityContract> entityContracts;
    private Entity entity;
    private Entity entity1;
    private Entity entity2;
    @BeforeEach
    void setUp() {
        // Set up mock behavior or data if needed before each test
        // Mock contracts associated with the entity

        card = Card.builder()
                .id(2L)
                .number("1234567890")
                .expiration("12/25")
                .cvv("123")
                .chargedAmount(new BigDecimal(123))
                .dailyBuyoutLimit(new BigDecimal(123))
                .activated(true)
                .activationDate(new Date(2023,1,1))
                .contract(Contract.builder()
                        .type(ContractType.CARD)
                        .deactivated(false)
                        .id(2L)
                        .build())
                .build();

        card2= Card.builder().id(1L).number("1234567890").expiration("12/25").cvv("123")
                .chargedAmount(new BigDecimal(123))
                .dailyBuyoutLimit(new BigDecimal(123))
                .activated(true)
                .activationDate(new Date(2023,1,1))
                .contract(Contract.builder()
                        .type(ContractType.CARD)
                        .deactivated(false)
                        .id(1L)
                        .build())
                .build();
        contract1 = Contract.builder().id(1L).card(card).type(ContractType.CARD).build();
        contract2 = Contract.builder().id(2L).card(card2).type(ContractType.ACCOUNT).build();


        contracts = Arrays.asList(contract1, contract2);
        entityContract1 = EntityContract.builder().contract(contract1).id(1L).build();
        entityContract2 = EntityContract.builder().contract(contract2).id(2L).build();
        entityContracts = Arrays.asList(entityContract1,entityContract2);

        entity1 = Entity.builder().contracts(entityContracts)
                .phoneConfirmed(true)
                .emailConfirmed(true)
                .verifyWithSign(true)
                .email("pepe@pepe.com")
                .type(EntityType.PHYSICAL).build();
        entity2 = Mockito.mock(Entity.class);
        entity = Mockito.mock(Entity.class);

    }
    @Test
    void testDeactivateCard() throws CustomException {

        when(entityUtils.checkIfEntityExists(any())).thenReturn(entity);
        when(cardRepository.findByNumber(anyString())).thenReturn(card);
        when(entity.getContracts()).thenReturn(entityContracts);

        // Call the method to test
        cardService.deactivateCard("1234567890");

        // Verify that the contract is deactivated and the card is set to not activated
        assertTrue(contract1.getDeactivated());
        assertFalse(card.getActivated());
        verify(cardRepository, times(1)).saveAndFlush(card);
        verify(entityContractRepository, times(1)).save(any(EntityContract.class));
    }
}

