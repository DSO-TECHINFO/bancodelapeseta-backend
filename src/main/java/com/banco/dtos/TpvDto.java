package com.banco.dtos;

import java.util.Date;

import com.banco.entities.Contract;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TpvDto {
    private String serialNumber;
    private String tpvCode;
}
