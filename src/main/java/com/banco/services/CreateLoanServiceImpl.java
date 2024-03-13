package com.banco.services;

import com.banco.dtos.LoanRequestDto;
import com.banco.entities.AmortizationPlan;
import com.banco.entities.ContractType;
import com.banco.entities.EntityContract;
import com.banco.entities.Loan;
import com.banco.mappers.LoanMapper;
import com.banco.repositories.AmortizationPlanRepository;
import com.banco.repositories.LoanRepository;
import com.banco.utils.EntityUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class CreateLoanServiceImpl implements CreateLoanService{

    EntityUtils entityUtils;
    LoanMapper loanMapper;
    AmortizationPlanRepository amortizationPlanRepository;
    CalculateAmortizationServiceImpl calculateAmortizationService;
    LoanRepository loanRepository;

    /**
     * TODO : Agregar Account y Contract al loan antes del save.
     *        Probar request
     * @param loanRequestDto
     */

    @Override
    public void loanCreation(LoanRequestDto loanRequestDto) {
        Loan loan = loanMapper.LoanRequestDtoToLoan(loanRequestDto);
        this.saveAmortizationPlan(loan);
        this.saveLoan(loan);
    }

    private void saveLoan(Loan loan){
        loan.setStartDate(Date.valueOf(LocalDate.now()));

        Date initialFinishDate = Date.valueOf(LocalDate.now().plusMonths(loan.getLoanNumberPayments()));
        loan.setInitialFinishDate(initialFinishDate);

        loan.setCurrentFinishDate(initialFinishDate);
        loan.setPaidAmount(new BigDecimal(BigInteger.ZERO));

        BigDecimal amountWithInterest = loan.getTotalAmount().multiply(loan.getInterestRate());
        loan.setUnpaidAmount(amountWithInterest);

        loan.setPendingAmount(amountWithInterest);
        loan.setPaidSubscriptions(0);
        loan.setUnpaidSubscription(loan.getLoanNumberPayments());

        loanRepository.save(loan);
    }
    private void saveAmortizationPlan(Loan loan){
        List <AmortizationPlan> amortizationPlans = calculateAmortizationService.calculateAmortization(loan);
        for (AmortizationPlan amortPlan: amortizationPlans) {
            amortizationPlanRepository.save(amortPlan);
        }
    }

    private List<EntityContract> getContract () {
         return entityUtils.checkIfEntityExists(entityUtils.extractUser()).getContracts()
                .stream().filter(contract -> contract.getContract().getType() == ContractType.ACCOUNT && !contract.getContract().getDeactivated())
                .collect(Collectors.toList());
    }

}

