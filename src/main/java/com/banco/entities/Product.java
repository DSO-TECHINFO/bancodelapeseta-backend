package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String name;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String description;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal fee;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private ProductType type;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private EntityType entityType;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private ProductRecurrenceType recurrence;
    @OneToOne
    private Currency currency;
}
