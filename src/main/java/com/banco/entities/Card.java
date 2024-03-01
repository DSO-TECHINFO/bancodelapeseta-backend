package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Contract contract;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String cvv;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String number;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String expiration;

    @JsonIgnore
    @Column
    private Double cashier_limit;

    @JsonIgnore
    @Column
    private Double daily_buyout_limit;

    @JsonIgnore
    @Column
    private Short activated;

    @JsonIgnore
    @Column
    private Date activation_date;
}
