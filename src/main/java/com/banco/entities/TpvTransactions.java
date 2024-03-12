package com.banco.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <h1>Entidad Transacciones TPV</h1>
 *
 * <p>Esta entidad representa cada transacción realizada por el TPV</p>
 *
 * <h3>Propiedades</h3>
 * <ul>
 * <li><b>id:</b> Identificador único de la PTV</li>
 * <li><b>tpv:</b> Terminal de punto de venta en el que realiza la transacción</li>
 * <li><b>card:</b> Tarjeta de crédito que se va a cobrar</li>
 * <li><b>transactionNumber:</b> Número serial de la transacción</li>
 * <li><b>confirmation:</b> Confirmación de si el usuario aceptó el cobro.</li>
 * </ul>
 */
@Entity
@Table(name = "tpv_transactions")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TpvTransactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    private Tpv tpv;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(cascade = CascadeType.REFRESH)
    private Card card;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String transactionNumber;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Boolean confirmation;
}
