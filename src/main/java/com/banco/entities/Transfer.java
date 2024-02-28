package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.util.Date;

@jakarta.persistence.Entity
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date date;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String beneficiaryName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String payerName;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Account account;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String destinationAccount;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String concept;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String description;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String externalReference;

    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private TransferStatus status;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal fee;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private BigDecimal amount;

    public boolean isPending(){
        return status.equals(TransferStatus.PENDING);
    }
}
