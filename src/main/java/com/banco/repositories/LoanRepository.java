package com.banco.repositories;

import com.banco.entities.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan,Long > {

    Loan findByContractId(Long contractId);
}
