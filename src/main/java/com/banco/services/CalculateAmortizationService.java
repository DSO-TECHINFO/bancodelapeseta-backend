package com.banco.services;

import com.banco.entities.AmortizationPlan;
import com.banco.entities.Loan;

import java.math.BigDecimal;
import java.util.List;

public interface CalculateAmortizationService {
   public List<AmortizationPlan> calculateAmortization(Loan loan);

   public BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int totalPayments);

   public void loanParamsValidation(Loan loan);
}
