package com.banco.repositories;

import com.banco.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String email);
    Integer deleteByEmail(String email);
}
