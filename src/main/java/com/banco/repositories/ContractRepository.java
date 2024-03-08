package com.banco.repositories;

import com.banco.entities.Contract;
import com.banco.entities.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long> {

}
