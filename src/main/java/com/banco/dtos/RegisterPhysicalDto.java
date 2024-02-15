package com.banco.dtos;

import com.banco.entities.EntityDebtType;
import com.banco.entities.EntityGender;
import com.banco.entities.EntityRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
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
public class RegisterPhysicalDto {

    private String name;

    private String surname;

    private LocalDate birthday;

    private String taxId;

    private String password;

    private Date nationalIdExpiration;

    private String address;

    private String addressAdditionalInfo;

    private String postalCode;

    private String addressTown;

    private String addressCity;

    private String addressCountry;

    private EntityGender gender;

    private String nationality;

    private String birthCity;

    private String phoneNumber;

    private String email;

    private EntityDebtType debtType;

}
