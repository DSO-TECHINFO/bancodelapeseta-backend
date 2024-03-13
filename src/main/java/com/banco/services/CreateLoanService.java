package com.banco.services;


import com.banco.dtos.LoanRequestDto;
import com.banco.entities.Loan;

public interface CreateLoanService {

    //crear funcionalidad para ir actualizando la entidad Loan luego de cada amortizacion.
    // tengo que guardar el prestamos en la tabla prestamo y en la TABLA CONTRATO . En la tabla contract tengo que crear unregistro nuevo donde ponga el mismo accoountId y guardar el loan_id en el cmapo loan_id

    public void loanCreation(LoanRequestDto loanRequestDto);
        // primero obtener el usuario para obtener el account
        // mappear de LoanDTO a loan.
        // luego calcular la amortizacion
        //

}
