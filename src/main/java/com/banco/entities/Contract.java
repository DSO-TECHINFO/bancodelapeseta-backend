package com.banco.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contract {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private ContractType type;

    @JsonIgnore
    @Column
    private Date creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToOne
    private Product product;

    @JsonIgnore
    @OneToMany(mappedBy = "contract",cascade = CascadeType.REMOVE)
    private List<EntityContract> entityContract;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH} ,orphanRemoval = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Account account;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Card card;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Loan loan;

    @OneToOne(mappedBy = "contract")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnore
    private Tpv tpv;

    @JsonIgnore
    @Column
    private Boolean deactivated;

}
