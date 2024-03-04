package com.banco;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.banco.entities.Card;
import com.banco.entities.Entity;
import com.banco.repositories.CardRepository;
import com.banco.repositories.EntityRepository;
import com.banco.security.JwtService;
import com.banco.services.CardService;

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
    private CardRepository cardRepository;
    @MockBean
    private EntityRepository entityRepository;

    
    @Test
    @WithMockUser
    void testCardListEmptyOk() throws Exception {
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .build()));
                        
        when(cardRepository.findAllCardsFromEntityTaxId(any())).thenReturn(new ArrayList<Card>());

        mockMvc.perform(MockMvcRequestBuilders
                    .get("/card")
                    .header("Authorization","Bearer " + jwtService.generateToken(new Entity()))) 
                .andExpect(MockMvcResultMatchers.content().string("[]"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser
    void testCardListOk() throws Exception {
        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .build()));

        List<Card> mockCards = new ArrayList<>();
        mockCards.add(Card.builder()
                .number("1234 5678 9012")
                .build());
        mockCards.add(Card.builder()
                .number("2345 6789 0123")
                .build());
        mockCards.add(Card.builder()
                .number("1234 5678 9012")
                .build());
        mockCards.add(Card.builder()
                .number("3456 7890 1234")
                .build());
        when(cardRepository.findAllCardsFromEntityTaxId(any())).thenReturn(mockCards);
        

        mockMvc.perform(MockMvcRequestBuilders
                    .get("/card")
                    .header("Authorization","Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(MockMvcResultMatchers.content().json(TestUtils.asJsonString(CardService.cardListToCardDtoList(mockCards))))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }
}
