package com.banco.services;

import java.util.Date;
import java.util.List;

import com.banco.entities.*;
import com.banco.repositories.ContractRepository;
import com.banco.utils.CopyNonNullFields;
import com.banco.utils.EntityUtils;

import com.banco.utils.ProductUtils;
import org.springframework.stereotype.Service;

import com.banco.dtos.TpvDto;
import com.banco.exceptions.CustomException;
import com.banco.repositories.TpvRepository;
import com.banco.repositories.TpvTransactionsRepository;

import java.util.Optional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TpvServiceImpl implements TpvService {

    private final TpvRepository tpvRepository;
    private final AccountService accountService;
    private final TpvTransactionsRepository tpvTransactionsRepository;
    private final ContractRepository contractRepository;
    private final CopyNonNullFields mapperService;
    private final EntityUtils entityUtils;
    private ProductUtils productUtils;

    @Override
    public List<TpvDto> getAll() throws CustomException {
        return tpvRepository.findByContractIds(entityUtils.checkIfEntityExists(entityUtils.extractUser())
            .getContracts()
            .stream()
            .filter(entityContract -> !entityContract.getContract().getDeactivated())
            .map(contract-> contract.getContract().getId()).toList()).stream().map(tpv -> {
                TpvDto dto = new TpvDto();
                mapperService.copyNonNullProperties(tpv, dto, false);
                return dto;
            }).toList();
    }

    @Override
    public void create(TpvDto dto, Long accountId, Long productId) throws CustomException {
        List<EntityContract> contractsWithAccount = entityUtils.checkIfEntityExists(entityUtils.extractUser())
            .getContracts().stream().filter(contract -> contract.getContract().getAccount().getId().equals(accountId)).toList();

        if(!contractsWithAccount.isEmpty()){
            Product product = productUtils.checkProduct(productUtils.extractProduct(productId));
            Tpv tpv = new Tpv();
            mapperService.copyNonNullProperties(dto, tpv, true);
            Contract contract = Contract.builder().creationDate(new Date()).account(accountService.getAccountById(accountId)).product(product).deactivated(false).type(ContractType.TPV).tpv(tpv).build();
            tpvRepository.save(tpv);
            contractRepository.save(contract);
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
            .orElseThrow( () ->
                new CustomException("NOT_FOUND", "No se ha encontrado esta transaccion", 404)
            );
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
