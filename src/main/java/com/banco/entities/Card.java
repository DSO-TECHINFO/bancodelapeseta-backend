package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String number;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String expiration;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String cvv;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Column(precision = 10, scale = 2)
    private BigDecimal cashierLimit;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Column(precision = 10, scale = 2)
    private BigDecimal dailyBuyoutLimit;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Boolean activated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date activationDate;
    
    @JsonIgnore
    @OneToOne
    private Contract contract;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name="type")
    private CardType cardType;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(precision = 10, scale = 2)
    private BigDecimal chargedAmount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String pin;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(precision = 10, scale = 2)
    private BigDecimal fee;
}
