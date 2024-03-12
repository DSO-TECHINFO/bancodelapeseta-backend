package com.banco.services;

import java.util.List;

import com.banco.entities.ContractType;
import com.banco.utils.CopyNonNullFields;
import com.banco.utils.EntityUtils;

import org.springframework.stereotype.Service;

import com.banco.dtos.TpvDto;
import com.banco.entities.Tpv;
import com.banco.entities.TpvTransactions;
import com.banco.exceptions.CustomException;
import com.banco.repositories.TpvRepository;
import com.banco.repositories.TpvTransactionsRepository;

import java.util.Optional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TpvServiceImpl implements TpvService {

    private final TpvRepository tpvRepository;
    private final TpvTransactionsRepository tpvTransactionsRepository;
    private final CopyNonNullFields mapperService;
    private final EntityUtils entityUtils;

    /**
     * TODO SE TRAE Tó
     *
     * todo  DANI MARICA Y JESUS TB (TABACO)
     * @return
     * @throws CustomException
     */
    @Override
    public List<TpvDto> getAll() throws CustomException {
        List<Long> contracts = entityUtils.checkIfEntityExists(entityUtils.extractUser())
            .getContracts()
            .stream()
            .filter(entityContract -> entityContract.getContract().getType().equals(ContractType.TPV) && !entityContract.getContract().getDeactivated())
            .map(contract -> contract.getId())
            .toList();

        return tpvRepository.findByContractIds(contracts)
            .stream()
            .map(tpv -> {
                TpvDto dto = new TpvDto();
                mapperService.copyNonNullProperties(tpv, dto, false);
                return dto;
            })
            .toList();
    }

    /**
     * TODO CREAR EL CONTRATO DE TIPO TPV PARA ESA CUENTA, TENIENDO 2 CONTRATOS POR CUENTA 1 DE CUENTA Y OTRO TPV MINIMO
     * TODO SE DEBE TENER EN CUENTA QUE DEBE ESTAR LIGADO ESE TPV A TODOS LOS CONTRATOS DE LA CUENTA AL MOMENTO DE AGREGARLO*
     * @param dto
     * @throws CustomException
     */
    @Override
    public void create(TpvDto dto) throws CustomException {
        Tpv tpv = new Tpv();
        mapperService.copyNonNullProperties(dto, tpv, true);
        tpvRepository.save(tpv);
    }

    /**
     * TODO SE RETORNA EL PAGO EN CASO DE CANCELAR LA CONFIRMACIÓN DEL FRONT, EN CASO CONTRARIO SE COBRA PERO SIEMPRE
     * TODO QUEDA CREADA LA TRANSACCIÓN DEL PAGO.
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
