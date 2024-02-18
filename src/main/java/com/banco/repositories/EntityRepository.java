package com.banco.repositories;

import com.banco.entities.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EntityRepository extends JpaRepository<Entity, Long> {

    Optional<Entity> findByTaxId(String username);
}
