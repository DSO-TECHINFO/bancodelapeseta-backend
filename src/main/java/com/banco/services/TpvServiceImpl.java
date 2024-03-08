package com.banco.services;

import java.util.List;

import com.banco.entities.ContractType;
import com.banco.mappers.TpvMapper;
import com.banco.repositories.EntityRepository;
import com.banco.utils.CopyNonNullFields;
import com.banco.utils.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banco.dtos.TpvDto;
import com.banco.entities.Tpv;

import com.banco.exceptions.CustomException;
import com.banco.repositories.TpvRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TpvServiceImpl implements TpvService {

    private final TpvRepository tpvRepository;
    private final CopyNonNullFields mapperService;
    private final EntityUtils entityUtils;

    //#region Implementación de interfaz TpvService
    @Override
    public List<TpvDto> getAll() throws CustomException {
        /*return entityUtils.checkIfEntityExists(entityUtils.extractUser())
                .getContracts().stream().filter(contract->contract.getContract().getType() == ContractType.TPV && !contract.getContract().getDeactivated())
                .toList().stream().map();*/
        return tpvRepository.findAll().stream().map(tpv->{
            TpvDto dto = new TpvDto();
            mapperService.copyNonNullProperties(tpv, dto, false);
            return dto;
        }).toList();
    }

    @Override
    public void create(TpvDto dto) throws CustomException {
        Tpv tpv = new Tpv();
        mapperService.copyNonNullProperties(dto, tpv, true);
        tpvRepository.save(tpv);
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
