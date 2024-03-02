package com.banco;

import com.banco.Factories.LoanFactory;
import com.banco.entities.Loan;
import com.banco.entities.LoanPayment;
import com.banco.services.CalculateAmortizationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
public class CalculateAmortizationTests {

    @InjectMocks
    CalculateAmortizationServiceImpl calculateAmortizationService;



    @Test
    public void calculateFrenchAmortizationSuccess(){
        Loan loan = new LoanFactory().sampleLoan();
        List<LoanPayment> loanPayments = calculateAmortizationService.calculateAmortization(loan);
        LoanPayment firstPayment = loanPayments.get(0);
        LoanPayment lastPayment = loanPayments.get(loan.getLoanNumberPayments()-1);

        Assertions.assertEquals(new BigDecimal("1024.715"), firstPayment.getPaymentAmount());
        Assertions.assertEquals(new BigDecimal("333.333"), firstPayment.getInterestPaid());

    }
}
