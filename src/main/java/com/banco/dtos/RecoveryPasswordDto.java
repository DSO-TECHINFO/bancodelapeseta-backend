package com.banco.dtos;

import com.banco.entities.EntityType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecoveryPasswordDto {

    private EntityType type;
    private String taxId;
    private Date nationalIdExpiration;
    private String phone;
    private Date birthday;
    private Date settingUpDate;
}
