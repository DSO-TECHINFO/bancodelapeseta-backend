package com.banco.dtos;

import java.util.Date;

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
    private Date activationDate;
    private Date deactivationDate;
    private Boolean activated;
}
