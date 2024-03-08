package com.banco.mappers;

import com.banco.utils.CopyNonNullFields;

import com.banco.dtos.TpvDto;
import com.banco.entities.Tpv;
import lombok.AllArgsConstructor;

/**
 * <h2>Mapper TpvMapper</h2>
 * <p>Mapeador de la entidad Tpv con su respectivo dto</p>
 */
@AllArgsConstructor
public class TpvMapper {

    private final CopyNonNullFields mapperService;

    public TpvDto convertToDto(Tpv tpv){
        TpvDto dto = new TpvDto();

        mapperService.copyNonNullProperties(tpv, dto, false);

        return dto;
    }

    public Tpv convertToEntity(TpvDto dto){
        Tpv tpv = new Tpv();

        mapperService.copyNonNullProperties(dto, tpv, false);

        return tpv;
    }
}
