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

/**
 * <h1>Entidad PTV</h1>
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
    * <li><b>contract:</b> Cuando lo sepamos te decimos máquina T_T</li>
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

    @JsonIgnore
    @OneToOne(mappedBy = "tpv")
    private Contract contract;
}
