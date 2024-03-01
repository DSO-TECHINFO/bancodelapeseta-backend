package com.banco.services;

import com.banco.entities.Contract;
import com.banco.entities.ContractType;
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
    public List<Contract> getAccounts() throws CustomException {

        return entityRepository.findByTaxId(entityUtils.checkIfEntityExists(entityUtils.extractUser()).getTaxId()).orElseThrow()
                .getContracts().stream().filter(contract -> contract.getType() == ContractType.ACCOUNT).collect(Collectors.toList());

    }
}
