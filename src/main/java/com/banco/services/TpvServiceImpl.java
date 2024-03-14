package com.banco.services;

import java.util.Date;
import java.util.List;

import com.banco.entities.*;
import com.banco.repositories.AccountRepository;
import com.banco.repositories.ContractRepository;
import com.banco.repositories.EntityContractRepository;
import com.banco.utils.CopyNonNullFields;
import com.banco.utils.EntityUtils;

import com.banco.utils.ProductUtils;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.banco.dtos.TpvDto;
import com.banco.exceptions.CustomException;
import com.banco.repositories.TpvRepository;
import com.banco.repositories.TpvTransactionsRepository;

import java.util.Optional;
import lombok.AllArgsConstructor;
import com.banco.dtos.TpvDtoCreate;

@Service
@AllArgsConstructor
public class TpvServiceImpl implements TpvService {

    private final TpvRepository tpvRepository;
    private final EntityContractRepository entityContractRepository;
    private final AccountService accountService;
    private final TpvTransactionsRepository tpvTransactionsRepository;
    private final ContractRepository contractRepository;
    private final AccountRepository accountRepository;
    private final CopyNonNullFields mapperService;
    private final EntityUtils entityUtils;
    private ProductUtils productUtils;

    @Override
    public List<TpvDto> getAll() throws CustomException {
        return tpvRepository.findByContractIds(entityUtils.checkIfEntityExists(entityUtils.extractUser())
            .getContracts().stream()
            .filter(entityContract -> !entityContract.getContract().getDeactivated())
            .map(contract-> contract.getContract().getId()).toList()).stream().map(tpv -> {
                TpvDto dto = new TpvDto();
                mapperService.copyNonNullProperties(tpv, dto, false);
                return dto;
            }).toList();
    }

    @Override
    public void create(TpvDtoCreate dtoCreate, Long accountId) throws CustomException {
        //Verificar que existan contratos para la cuenta recibida.
        entityUtils.checkIfEntityExists(entityUtils.extractUser())
            .getContracts().stream()
            .filter(account-> account.getContract().getAccount().getId().equals(accountId))
            .findFirst().orElseThrow(()->{
                throw new CustomException("NOT_FOUND", "No se ha Encontrado la cuenta", 404);
            });

        //Entidades necesarias.
        Tpv tpv = new Tpv();
        Product product = productUtils.checkProduct(productUtils.extractProduct(dtoCreate.getProductId()));

        //Generar el tpv_code automático para el usuario teniendo en cuenta los que ya tiene.
        tpv.setTpvCode(String.format("tpv_", entityUtils.checkIfEntityExists(entityUtils.extractUser()).getContracts().stream()
            .filter(account->account.getContract().getType().equals(ContractType.TPV)).count()+1));
            mapperService.copyNonNullProperties(dtoCreate, tpv, false);
        Contract contract = Contract.builder().creationDate(new Date()).account(accountService.getAccountById(accountId)).product(product).deactivated(false).type(ContractType.TPV).tpv(tpv).build();

        //Si no existe un contrato con ese tpv para ese usuario, se genera todo.
        boolean contractWithTpv = entityUtils.checkIfEntityExists(entityUtils.extractUser()).getContracts().stream()
            .filter(account->account.getContract().getTpv() != null && account.getContract().getTpv().getTpvCode().equals(tpv.getTpvCode())).count() > 0;

        if(!contractWithTpv){
            EntityContract entityContract = EntityContract.builder().contract(contract).entity(entityUtils.checkIfEntityExists(entityUtils.extractUser())).role(dtoCreate.getRole()).build();
            tpvRepository.save(tpv);
            //contractRepository.save(contract);
            entityContractRepository.save(entityContract);
        }
    }

    /**
     * TODO SE RETORNA EL PAGO EN CASO DE CANCELAR LA CONFIRMACIÓN DEL FRONT, EN CASO CONTRARIO SE COBRA PERO SIEMPRE
     * TODO QUEDA CREADA LA TRANSACCIÓN DEL PAGO.
     *
     * TODO YERA PON EL MÉTODO CON LA FIRMA BIEN CABRON Y BORRA ESTE COMENTARIO.
     * @param idTransaction Identificador único de la transacción.
     * @throws CustomException
     */
    @Override
    public void returnPayment(Long idTransaction) throws CustomException {
        TpvTransactions tpvTransaction = tpvTransactionsRepository.findById(idTransaction)
            .orElseThrow( () -> new CustomException("NOT_FOUND", "No se ha encontrado esta transaccion", 404));

        if (tpvTransaction.getDevuelto()) {
            throw new CustomException("UNHANDLED-001", "Ya se ha devuelto", 406);
        }

        if (tpvTransaction.getConfirmation()) {
            Account dealerAccount = tpvTransaction.getTpv().getContract().getAccount();

            if (dealerAccount.getBalance().compareTo(tpvTransaction.getAmount()) <= 0) {
                throw new CustomException("UNHANDLED-002", "No hay fondos suficientes para hacer la devolucion", 406);
            }

            dealerAccount.setBalance(dealerAccount.getBalance().subtract(tpvTransaction.getAmount()));
            dealerAccount.setBalance(dealerAccount.getReal_balance().subtract(tpvTransaction.getAmount()));
            accountRepository.save(dealerAccount);
        }

        Account cuentaTarjetaPago = contractRepository.findByCard(tpvTransaction.getCard()).getAccount();
        cuentaTarjetaPago.setBalance(cuentaTarjetaPago.getBalance().add(tpvTransaction.getAmount()));
        cuentaTarjetaPago.setReal_balance(cuentaTarjetaPago.getReal_balance().add(tpvTransaction.getAmount()));
        accountRepository.save(cuentaTarjetaPago);

        tpvTransaction.setDevuelto(true);
        tpvTransactionsRepository.save(tpvTransaction);
    }

    @Override
    public void activate(Long id) throws CustomException {
        if(tpvRepository.existsById(id)){
            Optional<Tpv> tpv = tpvRepository.findById(id);
            if(tpv.isPresent()){
                tpv.get().setActivated(true);
                tpvRepository.save(tpv.get());
            }
        }
    }

    @Override
    public void deactivate(Long id) throws CustomException {
        if(tpvRepository.existsById(id)){
            Optional<Tpv> tpv = tpvRepository.findById(id);
            if(tpv.isPresent()){
                tpv.get().setActivated(false);
                tpvRepository.save(tpv.get());
            }
        }
    }
    //#endregion
}
