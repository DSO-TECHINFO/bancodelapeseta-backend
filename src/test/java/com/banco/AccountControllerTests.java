package com.banco;

import com.banco.dtos.PasswordChangeDto;
import com.banco.entities.Entity;
import com.banco.repositories.EntityRepository;
import com.banco.security.JwtService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Date;
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
}
