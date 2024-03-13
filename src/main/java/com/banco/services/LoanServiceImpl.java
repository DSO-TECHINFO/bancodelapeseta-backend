package com.banco.services;

import com.banco.dtos.LoanDto;
import com.banco.dtos.LoanRequestDto;
import com.banco.entities.*;
import com.banco.exceptions.CustomException;
import com.banco.mappers.LoanMapper;
import com.banco.repositories.AmortizationPlanRepository;
import com.banco.repositories.ContractRepository;
import com.banco.repositories.LoanRepository;
import com.banco.utils.EntityUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class LoanServiceImpl implements LoanService{

    LoanRepository loanRespoitory;
    EntityUtils entityUtils;

    LoanMapper loanMapper;
    AmortizationPlanRepository amortizationPlanRepository;
    CalculateAmortizationServiceImpl calculateAmortizationService;
    LoanRepository loanRepository;

    ContractRepository contractRepository;

    @Override
    public List<LoanDto> getLoans() {
        List<EntityContract> entityContracts = this.entityLoanContracts();
        List<LoanDto> loans = new ArrayList<>();
        for (EntityContract contract: entityContracts) {
            loanMapper.loanToLoanDto(loanRespoitory.findByContractId(contract.getId()));
            loans.add(loanMapper.loanToLoanDto(loanRespoitory.findByContractId(contract.getId())));
        }
        return loans;
    }

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


        Contract contract = Contract.builder()
                .creationDate(new java.util.Date())
                .account(new Account())
                .deactivated(false)
                .type(ContractType.ACCOUNT)
                .build();
        contractRepository.save(contract);
        loanRepository.save(loan);
    }
    private void saveAmortizationPlan(Loan loan){
        List <AmortizationPlan> amortizationPlans = calculateAmortizationService.calculateAmortization(loan);
        for (AmortizationPlan amortPlan: amortizationPlans) {
            amortizationPlanRepository.save(amortPlan);
        }
    }



    private List<EntityContract> entityLoanContracts() throws CustomException {
        return entityUtils.checkIfEntityExists(entityUtils.extractUser()).getContracts()
                .stream().filter(contract -> contract.getContract().getType() == ContractType.LOAN && !contract.getContract().getDeactivated())
                .collect(Collectors.toList());
    }

}
