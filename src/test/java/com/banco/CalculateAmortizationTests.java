package com.banco;

import com.banco.Factories.LoanFactory;
import com.banco.entities.AmortizationPlan;
import com.banco.entities.Loan;
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
        List<AmortizationPlan> loanPayments = calculateAmortizationService.calculateAmortization(loan);
        AmortizationPlan firstPayments = loanPayments.get(0);
        AmortizationPlan lastPayment = loanPayments.get(loan.getLoanNumberPayments()-1);

        Assertions.assertEquals(new BigDecimal("333.33"), firstPayments.getInterests());
        Assertions.assertEquals(new BigDecimal("691.38"), firstPayments.getAmortCap());
        Assertions.assertEquals(new BigDecimal("9308.62"), firstPayments.getPendingCapital());

        Assertions.assertEquals(new BigDecimal("33.06"), lastPayment.getInterests());
        Assertions.assertEquals(new BigDecimal("991.66"), lastPayment.getAmortCap());
        Assertions.assertEquals(new BigDecimal("0.00"), lastPayment.getPendingCapital());
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
