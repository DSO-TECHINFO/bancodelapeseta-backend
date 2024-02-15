package com.banco.dtos;

import com.banco.entities.EntityDebtType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCompanyDto {


    private String name;

    private String taxId;

    private String address;

    private String addressAdditionalInfo;

    private String postalCode;

    private String addressTown;

    private String addressCity;

    private String addressCountry;

    private String password;

    private String phoneNumber;

    private String email;

    private EntityDebtType debtType;

    private Date settingUpDate;
}
