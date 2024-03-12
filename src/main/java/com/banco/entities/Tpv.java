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

/**
 * <h1>Entidad TPV</h1>
 *
 * <p>Esta entidad representa un terminal de punto de venta (TPV) como sistema
 * de pagos en línea dependiente de los contratos.</p>
 *
 * <h3>Propiedades</h3>
 * <ul>
 * <li><b>id:</b> Identificador único de la PTV</li>
 * <li><b>serialNumber:</b> Número de serie</li>
 * <li><b>tpvCode:</b> Código de desbloqueo</li>
 * <li><b>activationDate:</b> Fecha de última activación</li>
 * <li><b>activationDate:</b> Fecha de última desactivación</li>
 * <li><b>activated:</b> Flag que indica si está activada la PTV</li>
 * <li><b>contract:</b> Contrato asignado al TPV</li>
 * <li><b>tpvTransactions:</b> Lista de transacciones asociadas a cada TPV</li>
 * </ul>
 */
@Entity
@Table(name = "tpv")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tpv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String serialNumber;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private String tpvCode;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date activationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Date deactivationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column
    private Boolean activated;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToOne(cascade = CascadeType.REFRESH)
    private Contract contract;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @OneToMany(mappedBy = "tpv", cascade = CascadeType.REMOVE)
    private List<TpvTransactions> tpvTransactions;
}
