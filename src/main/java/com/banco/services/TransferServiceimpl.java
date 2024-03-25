package com.banco.services;

import com.banco.dtos.CancelTransferDto;
import com.banco.dtos.CreateTransferDto;
import com.banco.entities.*;
import com.banco.exceptions.CustomException;
import com.banco.repositories.AccountRepository;
import com.banco.repositories.TransferRepository;
import com.banco.utils.EntityUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@AllArgsConstructor
public class TransferServiceimpl implements TransferService{

    private CurrencyExchangeService currencyExchangeService;
    private EntityUtils entityUtils;
    private TransferRepository transferRepository;
    private VerifyService verifyService;
    private AccountRepository accountRepository;

    @Override
    public Map<String, List<Transfer>> listTransfers() {
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        Map<String, List<Transfer>> transferMap = new HashMap<>();
        user.getContracts().forEach(entityContract -> {
            if(entityContract.getContract().getType() == ContractType.ACCOUNT)
                transferMap.put(entityContract.getContract().getAccount().getAccountNumber(), transferRepository.findAllByPayerAccount(entityContract.getContract().getAccount().getAccountNumber()));
        });
        return transferMap;
    }

    @Override
    @Transactional
    public void createTransfer(CreateTransferDto createTransferDto){
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        if(createTransferDto.getAmount().compareTo(new BigDecimal(1)) < 0){
            throw new CustomException("TRANSFERS-002", "Minimum transfer amount is 1 " + createTransferDto.getCurrency(), 400);
        }
        verifyService.verifyTransactionCode(createTransferDto.getVerificationCode(), true);
        EntityContract entityContract = user.getContracts().stream().filter(entityContractFor ->
                (entityContractFor.getRole() == EntityContractRole.OWNER
                        || entityContractFor.getRole() == EntityContractRole.CO_OWNER
                        || entityContractFor.getRole() == EntityContractRole.AUTHORIZED)
                    && entityContractFor.getContract().getType() == ContractType.ACCOUNT
                    && entityContractFor.getContract().getAccount().getAccountNumber().equals(createTransferDto.getPayerAccount())
                    && entityContractFor.getContract().getAccount().getBalance().compareTo(createTransferDto.getAmount()) >= 0
        ).findAny().orElseThrow(()->new CustomException("TRANSFERS-001", "You cannot create that transfer", 400));

        entityContract.getContract().getAccount().setBalance(entityContract.getContract().getAccount().getBalance().add(createTransferDto.getAmount().negate()));
        accountRepository.save(entityContract.getContract().getAccount());


        Transfer transfer = Transfer.builder()
                .status(TransferStatus.PENDING)
                .fee(new BigDecimal(0))
                .payerName(user.getName())
                .concept(createTransferDto.getConcept())
                .description(createTransferDto.getDescription())
                .destinationAccount(createTransferDto.getDestinationAccount())
                .beneficiaryName(createTransferDto.getBeneficiaryName())
                .payerAccount(createTransferDto.getPayerAccount())
                .date(new Date())
                .amount(createTransferDto.getAmount())
                .currency(entityContract.getContract().getAccount().getCurrency().getCurrency())
                .build();

        transferRepository.save(transfer);
    }

    @Override
    public void cancelTransfer(CancelTransferDto cancelTransferDto) {
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        Transfer transfer = transferRepository.findById(cancelTransferDto.getTransferId()).orElseThrow(()-> new CustomException("TRANSFERS-003", "Transfer not found", 400));
        if(transfer.getStatus() != TransferStatus.PENDING)
            throw new CustomException("TRANSFERS-005", "You cannot cancel this transaction",400);
        verifyService.verifyTransactionCode(cancelTransferDto.getVerificationCode(), true);
        EntityContract entityContract = user.getContracts().stream().filter(entityContractTmp -> entityContractTmp.getContract().getType() == ContractType.ACCOUNT
                && entityContractTmp.getContract().getAccount().getAccountNumber().equals(transfer.getPayerAccount())
                && (entityContractTmp.getRole() == EntityContractRole.OWNER
                        || entityContractTmp.getRole() == EntityContractRole.CO_OWNER
                        || entityContractTmp.getRole() == EntityContractRole.AUTHORIZED)
                )
                .findAny()
                .orElseThrow(()-> new CustomException("TRANSFERS-004", "You cannot cancel this transaction", 400));
        entityContract.getContract().getAccount().setBalance(entityContract.getContract().getAccount().getBalance().add(transfer.getAmount()));
        accountRepository.save(entityContract.getContract().getAccount());

        transfer.setStatus(TransferStatus.CANCELLED);

        transferRepository.save(transfer);
    }

}
