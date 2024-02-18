package com.banco.repositories;

import com.banco.entities.EntityRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<EntityRole, Long> {
}
