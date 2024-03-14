package com.banco.dtos;

import com.banco.entities.EntityContractRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TpvDtoCreate {
    private EntityContractRole role;
    private Long productId;
    private String serialNumber;
}
