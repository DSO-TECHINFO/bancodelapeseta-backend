package com.banco.mappers;

import com.banco.dtos.LoanDto;
import com.banco.entities.Loan;
import org.mapstruct.Mapper;


@Mapper
public interface LoanMapper {
    LoanDto loanToLoanDto(Loan loan);
}
