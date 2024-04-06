package com.banco;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Optional;

import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;
import com.banco.security.JwtService;
import com.banco.services.TpvService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TpvControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @MockBean
    private TpvService tpvService;

    @MockBean
    private EntityRepository entityRepository;

    @WithMockUser
    @Test
    public void returnPayment_ValidTransaction_ReturnsOkResponse() throws Exception {
        // Arrange
        Long idTransaction = 1L;

        when(entityRepository.findByTaxId(any()))
                .thenReturn(
                        Optional.of(
                                Entity.builder()
                                        .contracts(new ArrayList<>())
                                        .emailConfirmed(true)
                                        .phoneConfirmed(true)
                                        .build()
                        )
                );

        mockMvc.perform(MockMvcRequestBuilders
                .get("/tpv/return/1")
                .header("Authorization", "Bearer " + jwtService.generateToken(new Entity())))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(tpvService, times(1)).returnPayment(idTransaction);
    }

    @WithMockUser
    @Test
    public void returnPayment_InvalidTransaction_ThrowsCustomException_NotFound() throws Exception {
        // Arrange
        Long idTransaction = 1L;

        when(entityRepository.findByTaxId(any()))
        .thenReturn(
                Optional.of(
                        Entity.builder()
                                .contracts(new ArrayList<>())
                                .emailConfirmed(true)
                                .phoneConfirmed(true)
                                .build()
                )
        );

        doThrow(new CustomException("NOT_FOUND", "No se ha encontrado esta transaccion", 404))
                .when(tpvService).returnPayment(idTransaction);

        // Act
        mockMvc.perform(MockMvcRequestBuilders
            .get("/tpv/return/1")
            .header("Authorization", "Bearer " + jwtService.generateToken(new Entity())))
            .andExpect(status().isNotFound())
            .andDo(MockMvcResultHandlers.print());

        // Assert
        verify(tpvService, times(1)).returnPayment(idTransaction);
    }

    @WithMockUser
    @Test
    public void returnPayment_InternalServerError_ThrowsException_AlreadyReturned() throws Exception {
        // Arrange
        Long idTransaction = 1L;

        when(entityRepository.findByTaxId(any()))
        .thenReturn(
                Optional.of(
                        Entity.builder()
                                .contracts(new ArrayList<>())
                                .emailConfirmed(true)
                                .phoneConfirmed(true)
                                .build()
                )
        );

        doThrow(new CustomException("UNHANDLED-001", "Ya se ha devuelto", 406))
                .when(tpvService).returnPayment(idTransaction);

        // Act
        mockMvc.perform(MockMvcRequestBuilders
            .get("/tpv/return/1")
            .header("Authorization", "Bearer " + jwtService.generateToken(new Entity())))
            .andExpect(status().isNotAcceptable())
            .andDo(MockMvcResultHandlers.print());

        // Assert
        verify(tpvService, times(1)).returnPayment(idTransaction);
    }
    
    @WithMockUser
    @Test
    public void returnPayment_InternalServerError_ThrowsException_NotEnoughtFunds() throws Exception {
        // Arrange
        Long idTransaction = 1L;

        when(entityRepository.findByTaxId(any()))
        .thenReturn(
                Optional.of(
                        Entity.builder()
                                .contracts(new ArrayList<>())
                                .emailConfirmed(true)
                                .phoneConfirmed(true)
                                .build()
                )
        );

        doThrow(new CustomException("UNHANDLED-002", "No hay fondos suficientes para hacer la devolucion", 406))
                .when(tpvService).returnPayment(idTransaction);

        // Act
        mockMvc.perform(MockMvcRequestBuilders
            .get("/tpv/return/1")
            .header("Authorization", "Bearer " + jwtService.generateToken(new Entity())))
            .andExpect(status().isNotAcceptable())
            .andDo(MockMvcResultHandlers.print());

        // Assert
        verify(tpvService, times(1)).returnPayment(idTransaction);
    }
}
