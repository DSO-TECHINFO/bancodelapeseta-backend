package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "entity_contract")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EntityContract {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    private Entity entity;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ManyToOne
    private Contract contract;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToOne
    private Role role;
    @Column
    private BigDecimal participationPercentage;
}
