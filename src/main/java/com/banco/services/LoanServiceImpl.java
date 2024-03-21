package com.banco.services;

import com.banco.entities.ContractType;
import com.banco.entities.EntityContract;
import com.banco.utils.EntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class LoanServiceImpl implements LoanService{

    EntityUtils entityUtils;


    @Override
    public List<EntityContract> getLoans() {
        return entityUtils.checkIfEntityExists(entityUtils.extractUser())
                .getContracts().stream().filter(contract -> contract.getContract().getType() == ContractType.LOAN && !contract.getContract().getDeactivated())
                .collect(Collectors.toList());
    }

}
