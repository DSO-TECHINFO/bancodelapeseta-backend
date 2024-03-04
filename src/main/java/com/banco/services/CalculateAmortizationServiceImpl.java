package com.banco.services;
import com.banco.entities.Loan;
import com.banco.entities.LoanPayment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculateAmortizationServiceImpl implements CalculateAmortizationService{


    @Override
    public List<LoanPayment> calculateAmortization(Loan loan)  {
        loanParamsValidation(loan);
        List<LoanPayment> payments = new ArrayList<>();
        BigDecimal remainingBalance = loan.getAmount();
        BigDecimal interestRate = loan.getInterestRate().divide(BigDecimal.valueOf(100));
        BigDecimal monthlyInterestRate = interestRate.divide(BigDecimal.valueOf(12), 7, RoundingMode.HALF_UP);
        int totalPayments = loan.getLoanNumberPayments();
        BigDecimal monthlyPayment = this.calculateMonthlyPayment(loan.getAmount(), monthlyInterestRate, totalPayments);

        for(int i = 0; i < totalPayments; ++i) {
            BigDecimal interestPayment = remainingBalance.multiply(monthlyInterestRate);
            BigDecimal principalPayment = monthlyPayment.subtract(interestPayment);
            remainingBalance = remainingBalance.subtract(principalPayment);
            LoanPayment payment = new LoanPayment();
            payment.setPaymentNumber(i + 1);
            payment.setPaymentAmount(monthlyPayment.setScale(2, RoundingMode.HALF_UP));
            payment.setInterestPaid(interestPayment.setScale(2, RoundingMode.HALF_UP));
            payment.setAmortization(principalPayment.setScale(2, RoundingMode.HALF_UP));
            payment.setRemainingBalance(remainingBalance.setScale(2, RoundingMode.HALF_UP));


            payments.add(payment);
        }

        return payments;
    }

    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int totalPayments) {
        BigDecimal numerator = loanAmount.multiply(monthlyInterestRate).multiply((BigDecimal.ONE.add(monthlyInterestRate)).pow(totalPayments));
        BigDecimal denominator = ((BigDecimal.ONE.add(monthlyInterestRate)).pow(totalPayments)).subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 7, RoundingMode.HALF_UP).setScale(7, RoundingMode.HALF_UP);
    }
    @Override
    public void loanParamsValidation(Loan loan) throws IllegalArgumentException{
        if (loan.getAmount().compareTo(BigDecimal.ONE) <= 0){
            throw  new IllegalArgumentException("The loan amount has to be equal or greater than 1");
        } else if (loan.getInterestRate().compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new IllegalArgumentException("The loan interest has to be greater than 0");
        } else if (loan.getLoanNumberPayments() < 1) {
            throw new IllegalArgumentException("The loan number of payments has to be equal or greater than 1");
        }
    }
}
