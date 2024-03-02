package com.banco.services;

import com.banco.entities.Loan;
import com.banco.entities.LoanPayment;

import java.math.BigDecimal;
import java.util.List;

public interface CalculateAmortizationService {
   public List<LoanPayment> calculateAmortization(Loan loan);

   public BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int totalPayments);

   public void loanParamsValidation(Loan loan);
}
