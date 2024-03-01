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
import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal balance;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal real_balance;
    @JsonIgnore
    @Column
    private Date creationDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String accountNumber;
    @JsonIgnore
    @Column
    private Boolean locked;

    @JsonIgnore
    @OneToOne
    private Contract contract;
}
