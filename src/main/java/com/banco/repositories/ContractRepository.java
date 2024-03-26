package com.banco.repositories;

import com.banco.entities.Contract;
import com.banco.entities.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query(value = """
        SELECT c.*
        FROM contract c
            INNER JOIN card cd
                ON c.id = cd.contract_id
        WHERE cd.number = :card
    """, nativeQuery = true)
    Contract findByCard(String card);
    
    Optional<Contract> findByAccountAccountNumberAndType(String payerAccount, ContractType contractType);
}
