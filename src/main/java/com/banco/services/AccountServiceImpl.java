package com.banco.services;

import com.banco.dtos.AddIntervenerToAccountDto;
import com.banco.dtos.CreateNewAccountDto;
import com.banco.dtos.RemoveIntervenerFromAccountDto;
import com.banco.dtos.VerificationCodeDto;
import com.banco.entities.*;
import com.banco.exceptions.CustomException;
import com.banco.repositories.*;
import com.banco.utils.AccountUtils;
import com.banco.utils.CurrencyUtils;
import com.banco.utils.EntityUtils;
import com.banco.utils.ProductUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
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
    private AccountUtils accountUtils;



    @Override
    public List<EntityContract> getAccounts() throws CustomException {

        return entityUtils.checkIfEntityExists(entityUtils.extractUser())
                .getContracts().stream().filter(contract -> contract.getContract().getType() == ContractType.ACCOUNT && !contract.getContract().getDeactivated())
                .collect(Collectors.toList());

    }

    @Override
    public Account getAccountById(Long accountId) throws CustomException {
        List<EntityContract> contracts = entityUtils.checkIfEntityExists(entityUtils.extractUser()).getContracts().stream()
                .filter(contract->contract.getContract().getType() == ContractType.ACCOUNT && !contract.getContract().getDeactivated())
                .toList();

        for (EntityContract c : contracts)
            if(c.getContract().getAccount().getId().equals(accountId))
                return c.getContract().getAccount();

        return null;
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
        Account account = Account.builder().currency(currency).accountNumber(accountUtils.createAccountNumber()).balance(new BigDecimal(0)).realBalance(new BigDecimal(0)).creationDate(new Date()).locked(false).build();
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
                throw new CustomException("ACCOUNTS-009", "You cannot do that, you must be the owner of the account to delete it", 400);
            if(entityContract.getContract().getAccount().getAccountNumber().equals(accountNumber)){
                entityContract.getContract().setDeactivated(true);
                entityContract.getContract().getAccount().setLocked(true);
                contractList.add(entityContract.getContract());
            }
        });

        if(contractList.stream().anyMatch(contract -> contract.getType() == ContractType.LOAN))
            throw new CustomException("ACCOUNTS-004", "You cannot deactivate that account because there is a loan associated to it", 400);
        if(account.isPresent() && account.get().getBalance().compareTo(new BigDecimal(0)) != 0 && account.get().getRealBalance().compareTo(new BigDecimal(0)) != 0)
            throw new CustomException("ACCOUNTS-005", "You cannot deactivate that account, you have balance on it", 400);
        if(transfers.stream().anyMatch(transfer -> transfer.getStatus() != TransferStatus.COMPLETED && transfer.getStatus() != TransferStatus.REJECTED))
            throw new CustomException("ACCOUNTS-006", "You cannot deactivate that account, at least one transfer is not completed", 400);
        if(contractList.stream().anyMatch(contract -> contract.getType() == ContractType.CARD && contract.getCard().getCardType() == CardType.PREPAID && contract.getCard().getChargedAmount().compareTo(new BigDecimal(0)) != 0))
            throw new CustomException("ACCOUNTS-007", "You cannot deactivate that account, your prepaid card must be empty", 400);
        if(contractList.isEmpty())
            throw new CustomException("ACCOUNTS-008", "Account not found", 404);

        contractRepository.saveAll(contractList);
    }

    @Override
    public void addIntervenerToAccount(AddIntervenerToAccountDto addIntervenerToAccountDto) {
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        if(addIntervenerToAccountDto.getRole() == EntityContractRole.OWNER)
            throw new CustomException("ACCOUNTS-013", "You cannot add owner to account", 400);
        if(user.getTaxId().equals(addIntervenerToAccountDto.getTaxId()))
            throw new CustomException("ACCOUNTS-015", "You cannot change your rol in the account", 400);
        verifyService.verifyTransactionCode(addIntervenerToAccountDto.getVerificationCode(), true);
        EntityContract entityContract = user.getContracts().stream().filter(entityContractFor ->
                entityContractFor.getContract().getAccount().getAccountNumber().equals(addIntervenerToAccountDto.getAccountNumber())
                    && !entityContractFor.getContract().getDeactivated()
                    && entityContractFor.getRole() == EntityContractRole.OWNER
        ).findFirst().orElseThrow(()-> new CustomException("ACCOUNTS-010","Account not found, deactivated or not owned",400));
        Optional<EntityContract> optionalEntityContract = entityContract.getContract().getEntityContract().stream().filter(entityContractFor-> entityContractFor.getEntity().getTaxId().equals(addIntervenerToAccountDto.getTaxId())).findAny();
        if(optionalEntityContract.isEmpty()){
            Entity userToAssign = entityRepository.findByTaxId(addIntervenerToAccountDto.getTaxId()).orElseThrow(()-> new CustomException("ACCOUNTS-012","User not found",404));
            entityContractRepository.save(EntityContract.builder().role(addIntervenerToAccountDto.getRole()).entity(userToAssign).contract(entityContract.getContract()).build());
            return;
        }
        optionalEntityContract.get().setRole(addIntervenerToAccountDto.getRole());
        entityContractRepository.save(optionalEntityContract.get());
    }
    @Override
    @Transactional
    public void removeIntervenerFromAccount(RemoveIntervenerFromAccountDto removeIntervenerFromAccountDto){
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        if(user.getTaxId().equals(removeIntervenerFromAccountDto.getTaxId()))
            throw new CustomException("ACCOUNTS-015", "You cannot remove yourself from account, please remove account", 400);
        verifyService.verifyTransactionCode(removeIntervenerFromAccountDto.getVerificationCode(), true);
        EntityContract entityContract = user.getContracts().stream().filter(entityContractFor ->
                entityContractFor.getContract().getAccount().getAccountNumber().equals(removeIntervenerFromAccountDto.getAccountNumber())
                        && !entityContractFor.getContract().getDeactivated()
                        && entityContractFor.getRole() == EntityContractRole.OWNER
        ).findFirst().orElseThrow(()-> new CustomException("ACCOUNTS-010","Account not found, deactivated or not owned",400));
        EntityContract entityContractFromUser = entityContract.getContract().getEntityContract().stream().filter(entityContractFor-> entityContractFor.getEntity().getTaxId().equals(removeIntervenerFromAccountDto.getTaxId())).findAny().orElseThrow(()-> new CustomException("ACCOUNT-014", "This user is not present in the contract", 404));
        entityContractRepository.delete(entityContractFromUser);
    }



}
