package com.banco.services;
import com.banco.entities.AmortizationPlan;
import com.banco.entities.Loan;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

@Service
public class CalculateAmortizationServiceImpl implements CalculateAmortizationService{

    /**
     * @param loan
     * @return
     * @throws IllegalArgumentException
     *
     * Calculates the Loan amortization using the French system.
     *  a List of AmortizationPlan with the information of each payment.
     * The attributes interestRate, loanAmount and loanNumberPayments cannot be zero.
     */
    @Override
    public List<AmortizationPlan> calculateAmortization(Loan loan)  {
        loanParamsValidation(loan);
        List<AmortizationPlan> amortizationPlans = new ArrayList<>();
        BigDecimal remainingBalance = loan.getAmount();
        BigDecimal interestRate = loan.getInterestRate().divide(BigDecimal.valueOf(100));
        BigDecimal monthlyInterestRate = interestRate.divide(BigDecimal.valueOf(12), 7, RoundingMode.HALF_UP);
        int totalPayments = loan.getLoanNumberPayments();
        BigDecimal monthlyPayment = this.calculateMonthlyPayment(loan.getAmount(), monthlyInterestRate, totalPayments);
        LocalDate actualDate = LocalDate.now();


        for(int i = 0; i < totalPayments; ++i) {
            BigDecimal interestPayment = remainingBalance.multiply(monthlyInterestRate);
            BigDecimal principalPayment = monthlyPayment.subtract(interestPayment);
            remainingBalance = remainingBalance.subtract(principalPayment);
            AmortizationPlan amortizationPlan = new AmortizationPlan();
            amortizationPlan.setRate(monthlyInterestRate);
            amortizationPlan.setInterests(interestPayment.setScale(2, RoundingMode.HALF_UP));
            amortizationPlan.setAmortCap(principalPayment.setScale(2, RoundingMode.HALF_UP));
            amortizationPlan.setPendingCapital(remainingBalance.setScale(2, RoundingMode.HALF_UP));
            amortizationPlan.setDate(Date.valueOf(actualDate.plusMonths(i + 1)));
            amortizationPlan.setLoan(loan);


            amortizationPlans.add(amortizationPlan);
        }

        return amortizationPlans;
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
