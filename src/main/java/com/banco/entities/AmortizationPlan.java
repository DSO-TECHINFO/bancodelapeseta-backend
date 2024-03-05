package com.banco.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "AmortizationPlan")
public class AmortizationPlan {
    @Id
    private Long id;

    private Date date;

    private BigDecimal rate;

    private BigDecimal amortCap;

    private BigDecimal interests;

    private BigDecimal pendingCapital;

    private Loan loan;



 /*
    id (Long): indica el id de la cuota.
    date(Date): indica la fecha de amortización de la cuota
    rate(BigDecimal(10,2)): Tasa de interés de la cuota
    amortCap(BigDecimal(10,2)): cantidad de capital amortizado en la cuota
    interests(BigDecimal(10,2)): cantidad de capital pagado de intereses en la cuota
    pendingCapital(BigDecimal(10,2)): cantidad de capital pendiente del préstamo neto tras pagar la cuota (Ej: si esta es la primera cuota y el total sin intereses son 100€ y el amortCap de esta cuota son 20€, este campo serìan 80€)
    loan(Loan): Préstamo asociado a la cuota
*/
}
