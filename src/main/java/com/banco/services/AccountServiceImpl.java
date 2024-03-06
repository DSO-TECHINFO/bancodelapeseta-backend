package com.banco.services;

import com.banco.dtos.CreateNewAccountDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.*;
import com.banco.exceptions.CustomException;
import com.banco.repositories.*;
import com.banco.utils.CurrencyUtils;
import com.banco.utils.EntityUtils;
import com.banco.utils.ProductUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    private AccountRepository accountRepository;
    private TransferRepository transferRepository;
    private ContractRepository contractRepository;
    @Override
    public List<EntityContract> getAccounts() throws CustomException {

        return entityUtils.checkIfEntityExists(entityUtils.extractUser())
                .getContracts().stream().filter(contract -> contract.getContract().getType() == ContractType.ACCOUNT && !contract.getContract().getDeactivated())
                .collect(Collectors.toList());

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
        Contract contract = Contract.builder().creationDate(new Date()).account(account).product(product).deactivated(false).type(ContractType.ACCOUNT).build();
        account.setContract(contract);
        EntityContract entityContract = EntityContract.builder().entity(entity).contract(contract).role(EntityContractRole.OWNER).build();
        entityContractRepository.save(entityContract);
    }

    @Override
    public void deactivateAccount(String accountNumber, VerificationCodeDto verificationCode) throws CustomException {
        Entity entity = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        verifyService.verifyTransactionCode(verificationCode.getVerificationCode(), true);
        List<Contract> contractList = new ArrayList<>();
        List<Transfer> transfers = transferRepository.findAllByPayerAccount(accountNumber);
        Optional<Account> account = accountRepository.findByAccountNumber(accountNumber);
        entity.getContracts().forEach(entityContract -> {
            if(entityContract.getRole() != EntityContractRole.OWNER && entityContract.getContract().getType() == ContractType.ACCOUNT && entityContract.getContract().getAccount().getAccountNumber().equals(accountNumber))
                throw new CustomException("ACCOUNTS-009", "You cannot do that, you must be the owner of the account to delete it.", 400);
            if(entityContract.getContract().getAccount().getAccountNumber().equals(accountNumber)){
                entityContract.getContract().setDeactivated(true);
                contractList.add(entityContract.getContract());
            }
        });

        if(contractList.stream().anyMatch(contract -> contract.getType() == ContractType.LOAN))
            throw new CustomException("ACCOUNTS-004", "You cannot deactivate that account because there is a loan associated to it", 400);
        if(account.isPresent() && account.get().getBalance().compareTo(new BigDecimal(0)) != 0 && account.get().getReal_balance().compareTo(new BigDecimal(0)) != 0)
            throw new CustomException("ACCOUNTS-005", "You cannot deactivate that account, you have balance on it", 400);
        if(transfers.stream().anyMatch(transfer -> transfer.getStatus() != TransferStatus.COMPLETED && transfer.getStatus() != TransferStatus.REJECTED))
            throw new CustomException("ACCOUNTS-006", "You cannot deactivate that account, at least one transfer is not completed", 400);
        if(contractList.stream().anyMatch(contract -> contract.getType() == ContractType.CARD && contract.getCard().getCardType() == CardType.PREPAID && contract.getCard().getChargedAmount().compareTo(new BigDecimal(0)) != 0))
            throw new CustomException("ACCOUNTS-007", "You cannot deactivate that account, your prepaid card must be empty", 400);
        if(contractList.isEmpty())
            throw new CustomException("ACCOUNTS-008", "Account not found", 404);
        contractRepository.saveAll(contractList);
    }
}
