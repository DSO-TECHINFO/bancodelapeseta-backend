package com.banco;

import com.banco.Factories.LoanFactory;
import com.banco.dtos.LoanRequestDto;
import com.banco.entities.*;
import com.banco.mappers.LoanMapper;
import com.banco.repositories.ContractRepository;
import com.banco.repositories.EntityRepository;
import com.banco.repositories.LoanRepository;
import com.banco.services.AccountServiceImpl;
import com.banco.services.CalculateAmortizationServiceImpl;
import com.banco.services.LoanServiceImpl;
import com.banco.utils.ProductUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoanServiceTests {

    @MockBean
    private EntityRepository entityRepository;

    @Mock
    private ProductUtils productUtils;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private AccountServiceImpl accountService;

    @Mock
    private CalculateAmortizationServiceImpl calculateAmortizationService;
    @InjectMocks
    private LoanServiceImpl loanService;


    @Test
    public void loanCreationSuccessfull(){
        LoanRequestDto loanRequestDto = new LoanFactory().sampleDto();
        Account account = new Account();
        account.setBalance(new BigDecimal("0.00"));
        account.setRealBalance(new BigDecimal("0.00"));

        Loan loan = new LoanFactory().sampleLoan();
        Product product = new Product();
        product.setActive(true);
        product.setId(2L);
        product.setType(ProductType.LOAN);

        Contract contract = Contract.builder()
                .creationDate(new java.util.Date())
                .product(product)
                .account(account)
                .deactivated(false)
                .type(ContractType.LOAN)
                .loan(loan)
                .build();

        when(entityRepository.findByTaxId(any()))
                .thenReturn(Optional.of(Entity.builder()
                        .contracts(new ArrayList<>())
                        .emailConfirmed(true)
                        .phoneConfirmed(false)
                        .build()));
        when(loanMapper.LoanRequestDtoToLoan(loanRequestDto)).thenReturn(new LoanFactory().sampleLoan());
        when(accountService.getAccountById(any())).thenReturn(account);
        when(productUtils.checkProduct(any())).thenReturn(product);
        when(loanRepository.save(loan)).thenReturn(loan);
        when(contractRepository.save(contract)).thenReturn(contract);
        loanService.loanCreation(loanRequestDto, 19L, 2L);
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(contractRepository, times(1)).save(any(Contract.class));
    }
}
