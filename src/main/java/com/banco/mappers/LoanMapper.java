package com.banco.mappers;

import com.banco.dtos.LoanDto;
import com.banco.dtos.LoanRequestDto;
import com.banco.entities.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper
public interface LoanMapper {
    LoanDto loanToLoanDto(Loan loan);

    @Mapping(target = "initialSubscriptionRate", source = "loanRequestDto.interestRate")
    @Mapping(target = "currentSubscriptionRate", source = "loanRequestDto.interestRate")
    Loan LoanRequestDtoToLoan(LoanRequestDto loanRequestDto);
}
