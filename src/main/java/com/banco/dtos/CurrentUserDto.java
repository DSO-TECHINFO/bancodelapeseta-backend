package com.banco.dtos;

import com.banco.entities.EntityContract;
import com.banco.entities.EntityDebtType;
import com.banco.entities.EntityGender;
import com.banco.entities.EntityType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String surname;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String taxId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date birthday;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String addressAdditionalInfo;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String postalCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String addressTown;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String addressCity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String addressCountry;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EntityGender gender;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nationality;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String birthCity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EntityType type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date creationDate;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phoneNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    private Boolean emailConfirmed;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean phoneConfirmed;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean signActivated;
}
