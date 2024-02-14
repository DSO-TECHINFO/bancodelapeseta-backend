package com.banco.repositories;

import com.banco.entities.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Entity, Long> {

    Optional<Entity> findByUsername(String email);
    Integer deleteByEmail(String email);
}
