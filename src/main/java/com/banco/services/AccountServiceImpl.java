package com.banco.services;

import com.banco.entities.ContractType;
import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;
import com.banco.utils.EntityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{
    private EntityRepository entityRepository;
    private EntityUtils entityUtils;
    @Override
    public List<EntityContract> getAccounts() throws CustomException {

        return entityRepository.findByTaxId(entityUtils.checkIfEntityExists(entityUtils.extractUser()).getTaxId()).orElseThrow()
                .getContracts().stream().filter(contract -> contract.getContract().getType() == ContractType.ACCOUNT).collect(Collectors.toList());

    }
}
