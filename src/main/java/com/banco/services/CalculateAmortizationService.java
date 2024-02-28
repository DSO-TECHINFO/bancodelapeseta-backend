package com.banco.services;
import com.banco.entities.Loan;
import com.banco.entities.LoanPayment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculateAmortizationService {


    public List<LoanPayment> calculateAmortizationSchedule(Loan loan) {
        List<LoanPayment> payments = new ArrayList<>();

        BigDecimal remainingBalance = loan.getAmount();
        BigDecimal monthlyInterestRate = loan.getInterestRate().divide(BigDecimal.valueOf(12), 7);
        int totalPayments = loan.getLoanMonthTerms();

        BigDecimal monthlyPayment = calculateMonthlyPayment(loan.getAmount(), monthlyInterestRate, totalPayments);



        return payments;
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int totalPayments) {
        BigDecimal monthlyPayment = loanAmount.multiply(monthlyInterestRate)
                .divide(BigDecimal.ONE.subtract(BigDecimal.ONE.add(monthlyInterestRate).pow(-totalPayments)), 2);
        return monthlyPayment;
    }
}
