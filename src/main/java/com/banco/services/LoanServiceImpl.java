package com.banco.services;

import com.banco.dtos.LoanDto;
import com.banco.entities.ContractType;
import com.banco.entities.EntityContract;
import com.banco.entities.Loan;
import com.banco.exceptions.CustomException;
import com.banco.mappers.LoanMapper;
import com.banco.repositories.LoanRepository;
import com.banco.utils.EntityUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

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

    private List<EntityContract> entityLoanContracts() throws CustomException {
        return entityUtils.checkIfEntityExists(entityUtils.extractUser()).getContracts()
                .stream().filter(contract -> contract.getContract().getType() == ContractType.LOAN && !contract.getContract().getDeactivated())
                .collect(Collectors.toList());
    }

}
