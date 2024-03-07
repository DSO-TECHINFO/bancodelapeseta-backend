package com.banco;

import com.banco.entities.Entity;
import com.banco.repositories.EntityRepository;
import com.banco.security.JwtService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CurrencyControllerTest {

    @MockBean
    private EntityRepository entityRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;
    @Test
    @WithMockUser
    @Transactional
    public void testCurrencyControllerTestIsOk() throws Exception {

        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .contracts(new ArrayList<>())
                        .emailConfirmed(true)
                        .phoneConfirmed(true)
                        .build()));


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/currency").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization","Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(status().isOk());
    }
}
