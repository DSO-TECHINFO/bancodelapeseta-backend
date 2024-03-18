package com.banco.Services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.banco.entities.Account;
import com.banco.entities.Contract;
import com.banco.entities.Tpv;
import com.banco.entities.TpvTransactions;
import com.banco.exceptions.CustomException;
import com.banco.repositories.AccountRepository;
import com.banco.repositories.ContractRepository;
import com.banco.repositories.TpvRepository;
import com.banco.repositories.TpvTransactionsRepository;
import com.banco.services.TpvServiceImpl;

public class TpvServiceTest {

    @Mock
    private TpvTransactionsRepository tpvTransactionsRepository;

    @Mock
    private TpvRepository tpvRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TpvServiceImpl tpvService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void returnPayment_SuccessfulTransaction_ReturnsNoException() throws CustomException {
        // Arrange
        Long idTransaction = 1L;
        TpvTransactions tpvTransaction = new TpvTransactions();
        tpvTransaction.setId(idTransaction);
        tpvTransaction.setDevuelto(false);
        tpvTransaction.setConfirmation(true);
        tpvTransaction.setAmount(new BigDecimal("50.00"));

        Account dealerAccount = new Account();
        dealerAccount.setBalance(new BigDecimal("100.00"));
        dealerAccount.setRealBalance(new BigDecimal("100.00"));

        Account cuentaTarjetaPago = new Account();
        cuentaTarjetaPago.setBalance(new BigDecimal("0.00"));
        cuentaTarjetaPago.setRealBalance(new BigDecimal("0.00"));

        Contract contract = new Contract();
        contract.setAccount(dealerAccount);

        Tpv tpv = new Tpv();
        tpv.setContract(new Contract());
        tpv.getContract().setAccount(dealerAccount);

        tpvTransaction.setTpv(tpv);

        when(tpvTransactionsRepository.findById(idTransaction)).thenReturn(Optional.of(tpvTransaction));
        when(contractRepository.findByCard(any())).thenReturn(contract);
        when(accountRepository.save(any())).thenReturn(new Account());

        // Act
        assertDoesNotThrow(() -> tpvService.returnPayment(idTransaction));

        // Assert
        assertTrue(tpvTransaction.getDevuelto());
    }

    @Test
    public void returnPayment_DevueltoTrue_ThrowsCustomException() {
        // Arrange
        Long idTransaction = 1L;
        TpvTransactions tpvTransaction = new TpvTransactions();
        tpvTransaction.setId(idTransaction);
        tpvTransaction.setDevuelto(true);

        when(tpvTransactionsRepository.findById(idTransaction)).thenReturn(Optional.of(tpvTransaction));

        // Act and Assert
        assertThrows(CustomException.class, () -> tpvService.returnPayment(idTransaction));
    }

    @Test
    public void returnPayment_InsufficientFunds_ThrowsCustomException() {
        // Arrange
        Long idTransaction = 1L;
        TpvTransactions tpvTransaction = new TpvTransactions();
        tpvTransaction.setId(idTransaction);
        tpvTransaction.setDevuelto(false);
        tpvTransaction.setConfirmation(true);
        tpvTransaction.setAmount(new BigDecimal("150.00"));

        Account dealerAccount = new Account();
        dealerAccount.setBalance(new BigDecimal("100.00"));
        dealerAccount.setRealBalance(new BigDecimal("100.00"));

        Contract contract = new Contract();
        contract.setAccount(dealerAccount);

        Tpv tpv = new Tpv();
        tpv.setContract(contract);
        
        tpvTransaction.setTpv(tpv);

        when(tpvTransactionsRepository.findById(idTransaction)).thenReturn(Optional.of(tpvTransaction));
        when(contractRepository.findByCard(any())).thenReturn(contract);

        // Act and Assert
        assertThrows(CustomException.class, () -> tpvService.returnPayment(idTransaction));
    }
}
