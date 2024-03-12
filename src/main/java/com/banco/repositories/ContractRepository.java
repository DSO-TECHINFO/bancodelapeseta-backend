package com.banco.repositories;

import com.banco.entities.Contract;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContractRepository extends JpaRepository<Contract, Long> {

	@Query("select u from Contract u where u.deactivated = true")
	Optional<List<Contract>> findContractDeactivated();
}
