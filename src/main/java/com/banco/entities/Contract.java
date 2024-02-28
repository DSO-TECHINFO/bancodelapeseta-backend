package com.banco.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
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
    private String type;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date creationDate;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToOne
    private Product product;
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "entity_contract", joinColumns = @JoinColumn(name = "contract_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "entity_id", referencedColumnName = "id"))
    private List<com.banco.entities.Entity> entities;
    @OneToMany(mappedBy = "contract")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Account> accounts;
    @OneToMany(mappedBy = "contract")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Card> cards;
    @OneToMany(mappedBy = "contract")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Loan> loans;
    @OneToMany(mappedBy = "contract")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Tpv> tpvs;

}
