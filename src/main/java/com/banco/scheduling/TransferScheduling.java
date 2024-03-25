package com.banco.scheduling;

import com.banco.entities.*;
import com.banco.repositories.AccountRepository;
import com.banco.repositories.ContractRepository;
import com.banco.repositories.TransferRepository;
import com.banco.services.CurrencyExchangeService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Component
public class TransferScheduling {
    private ContractRepository contractRepository;
    private TransferRepository transferRepository;
    private CurrencyExchangeService currencyExchangeService;

    @Scheduled(cron = "${transfer.scheduling.cron}")
    public void changeTransferStatus(){
        List<Transfer> transferList = transferRepository.findAllByStatus(TransferStatus.PENDING);
        for(Transfer transfer:transferList){
            try {
                Contract contractDestination = contractRepository.findByAccountAccountNumberAndType(transfer.getDestinationAccount(), ContractType.ACCOUNT).orElseThrow();
                BigDecimal rate = currencyExchangeService.getCurrencyExchangeRate(transfer.getCurrency()).getRates().get(contractDestination.getAccount().getCurrency().getCurrency());
                contractDestination.getAccount().setBalance(contractDestination.getAccount().getBalance().add(transfer.getAmount().multiply(rate)));
                contractDestination.getAccount().setRealBalance(contractDestination.getAccount().getRealBalance().add(transfer.getAmount().multiply(rate)));
                contractRepository.save(contractDestination);
                transfer.setStatus(TransferStatus.COMPLETED);
                Contract contractPayer = contractRepository.findByAccountAccountNumberAndType(transfer.getPayerAccount(), ContractType.ACCOUNT).orElseThrow();
                contractPayer.getAccount().setRealBalance(contractPayer.getAccount().getRealBalance().add(transfer.getAmount().negate()));
                contractRepository.save(contractPayer);
            }catch (Exception e){
                transfer.setStatus(TransferStatus.REJECTED);
                Contract contract = contractRepository.findByAccountAccountNumberAndType(transfer.getPayerAccount(), ContractType.ACCOUNT).orElseThrow();
                contract.getAccount().setBalance(contract.getAccount().getBalance().add(transfer.getAmount()));
                contractRepository.save(contract);
            }
        }

            transferRepository.saveAll(transferList);

    }
}
