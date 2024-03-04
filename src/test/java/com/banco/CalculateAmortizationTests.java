package com.banco;

import com.banco.Factories.LoanFactory;
import com.banco.entities.Loan;
import com.banco.entities.LoanPayment;
import com.banco.exceptions.CustomException;
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

        Assertions.assertEquals(new BigDecimal("1024.71"), firstPayment.getPaymentAmount());
        Assertions.assertEquals(new BigDecimal("333.33"), firstPayment.getInterestPaid());
        Assertions.assertEquals(new BigDecimal("691.38"), firstPayment.getAmortization());
        Assertions.assertEquals(new BigDecimal("9308.62"), firstPayment.getRemainingBalance());

        Assertions.assertEquals(new BigDecimal("1024.71"), lastPayment.getPaymentAmount());
        Assertions.assertEquals(new BigDecimal("33.06"), lastPayment.getInterestPaid());
        Assertions.assertEquals(new BigDecimal("991.66"), lastPayment.getAmortization());
        Assertions.assertEquals(new BigDecimal("0.00"), lastPayment.getRemainingBalance());
    }

    @Test
    public void calculateAmortizationFailWithLoanAmountZero (){
        Loan loan = new LoanFactory().sampleCustomLoan(12,new BigDecimal("0"),new BigDecimal("0.40"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calculateAmortizationService.calculateAmortization(loan));
    }

    @Test
    public void calculateAmortizationFailWithInterestRateZero (){
        Loan loan = new LoanFactory().sampleCustomLoan(12,new BigDecimal("100"),new BigDecimal("0"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calculateAmortizationService.calculateAmortization(loan));
    }

    @Test
    public void calculateAmortizationFailWithLoanNumberPaymentsZero (){
        Loan loan = new LoanFactory().sampleCustomLoan(0,new BigDecimal("100"),new BigDecimal("0.40"));

        Assertions.assertThrows(IllegalArgumentException.class, () -> calculateAmortizationService.calculateAmortization(loan));
    }
}
