package com.banco.services;

import com.banco.entities.Entity;
import com.banco.entities.Loan;
import com.banco.repositories.LoanRepository;
import com.banco.dtos.ListingLoanDto;
import lombok.AllArgsConstructor;
import com.banco.utils.EntityUtils;
import com.banco.exceptions.CustomException;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private LoanRepository loanRepository;
    private final EntityUtils entityUtils;

    @Override
    public List<ListingLoanDto> getUserLoans() throws CustomException {
        Entity user = entityUtils.checkIfEntityExists(entityUtils.extractUser());
        List<Loan> loanList = loanRepository.findAllLoansForUser(user.getTaxId());
        return LoanService.loanListToLoanDtoList(loanList);
    }

}
