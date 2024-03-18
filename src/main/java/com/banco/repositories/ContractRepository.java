package com.banco.repositories;

import com.banco.entities.Contract;
import com.banco.entities.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    Optional<Contract> findByAccountAccountNumberAndType(String payerAccount, ContractType contractType);
}
