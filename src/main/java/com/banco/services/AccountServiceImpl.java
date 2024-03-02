package com.banco.services;

import com.banco.dtos.CreateNewAccountDto;
import com.banco.entities.*;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityContractRepository;
import com.banco.repositories.EntityRepository;
import com.banco.repositories.ProductRepository;
import com.banco.utils.CurrencyUtils;
import com.banco.utils.EntityUtils;
import com.banco.utils.ProductUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountServiceImpl implements AccountService{
    private EntityRepository entityRepository;
    private EntityUtils entityUtils;
    private ProductUtils productUtils;
    private CurrencyUtils currencyUtils;
    private VerifyService verifyService;
    private EntityContractRepository entityContractRepository;
    @Override
    public List<EntityContract> getAccounts() throws CustomException {

        return entityRepository.findByTaxId(entityUtils.checkIfEntityExists(entityUtils.extractUser()).getTaxId()).orElseThrow()
                .getContracts().stream().filter(contract -> contract.getContract().getType() == ContractType.ACCOUNT).collect(Collectors.toList());

    }

    @Override
    public void createAccount(CreateNewAccountDto createNewAccountDto) throws CustomException {
        Entity entity = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        Product product = productUtils.checkProduct(productUtils.extractProduct(createNewAccountDto.getProductId()));
        Currency currency = currencyUtils.checkCurrency(currencyUtils.extractCurrency(createNewAccountDto.getCurrency()));
        verifyService.verifyTransactionCode(createNewAccountDto.getVerificationCode(), true);
        if(!product.getActive())
            throw new CustomException("ACCOUNTS-001", "This product is not available yet", 400);
        if(product.getType() != ProductType.ACCOUNT)
            throw new CustomException("ACCOUNTS-002", "This product is not eligible to create an account", 400);
        if(product.getEntityType() != entity.getType())
            throw new CustomException("ACCOUNTS-003", "You cannot chose that product", 400);
        Account account = Account.builder().currency(currency).accountNumber("ES" + RandomStringUtils.randomNumeric(18)).balance(new BigDecimal(0)).real_balance(new BigDecimal(0)).creationDate(new Date()).locked(false).build();
        Contract contract = Contract.builder().creationDate(new Date()).account(account).product(product).type(ContractType.ACCOUNT).build();
        account.setContract(contract);
        EntityContract entityContract = EntityContract.builder().entity(entity).contract(contract).role(EntityContractRole.OWNER).build();
        entityContractRepository.save(entityContract);
    }
}
