package com.banco;



import com.banco.entities.*;
import com.banco.repositories.EntityRepository;
import com.banco.repositories.LoanRepository;
import com.banco.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanControllerTests {

    @MockBean
    private EntityRepository entityRepository;

    @MockBean
    private LoanRepository loanRepository;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private JwtService jwtService;

    @Test
    @WithMockUser
    public void getLoansOkTest() throws Exception {
        Loan loan = Loan.builder()
                .amount(new BigDecimal("10000"))
                .totalAmount(new BigDecimal("10500"))
                .interestRate(new BigDecimal("0.05"))
                .initialSubscriptionRate(new BigDecimal("0.04"))
                .currentSubscriptionRate(new BigDecimal("0.04"))
                .startDate(new Date())
                .initialFinishDate(new Date())
                .currentFinishDate(new Date())
                .paidAmount(new BigDecimal("500"))
                .unpaidAmount(new BigDecimal("10000"))
                .pendingAmount(new BigDecimal("10000"))
                .paidSubscriptions(0)
                .unpaidSubscription(12)
                .status(LoanStatus.UNCOMPLETED)
                .creationStatus(LoanCreationStatus.CHOSE_LOAN_TYPE)
                .nextPayment(new Date())
                .interestType(LoanInterestType.FIXED)
                .subscriptionPeriodicity(LoanSubscriptionPeriodicity.MONTHLY)
                .amortizeInterest(new BigDecimal("100"))
                .loanType(LoanType.MORTGAGE)
                .loanNumberPayments(12)
                .build();

        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .contracts(new ArrayList<>())
                        .emailConfirmed(true)
                        .phoneConfirmed(false)
                        .build()));

        when(loanRepository.findByContractId(any())).thenReturn(loan);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/loans").contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + jwtService.generateToken(new Entity())));

    }
/*
    @Test
    @WithMockUser
    public void createLoan_ValidRequest_ReturnsOkResponse() throws Exception {
        // Arrange
        Long accountId = 1L;
        Long productId = 2L;
        LoanRequestDto loanRequestDto = new LoanRequestDto(); // Puedes configurar este objeto según sea necesario

        // Simulamos el comportamiento del servicio
        // Puedes configurar el comportamiento esperado según tus necesidades
        when(loanService.loanCreation(any(LoanRequestDto.class), any(Long.class), any(Long.class))).thenReturn(null);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/loans/create/" + accountId + "/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(loanRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verificamos que el método loanCreation del servicio haya sido llamado
        verify(loanService, times(1)).loanCreation(any(LoanRequestDto.class), any(Long.class), any(Long.class));
    }
 */
}
