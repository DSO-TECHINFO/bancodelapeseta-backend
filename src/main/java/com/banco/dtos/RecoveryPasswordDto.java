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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private EntityType type;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String nif;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date nationalIdExpiration;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date birthdate;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date settingUpDate;
}
