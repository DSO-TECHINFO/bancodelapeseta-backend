package com.banco.repositories;

import com.banco.entities.Loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("""
                SELECT l
                FROM Loan l
                JOIN l.contract contract
                JOIN contract.entityContract ec
                JOIN ec.entity e
                WHERE e.taxId = :taxId
            """)
    List<Loan> findAllLoansForUser(String taxId);

}
