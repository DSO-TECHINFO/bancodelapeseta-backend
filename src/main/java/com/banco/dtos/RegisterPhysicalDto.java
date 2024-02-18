package com.banco.dtos;

import com.banco.entities.EntityDebtType;
import com.banco.entities.EntityGender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPhysicalDto {

    private String name;

    private String surname;

    private Date birthday;

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
