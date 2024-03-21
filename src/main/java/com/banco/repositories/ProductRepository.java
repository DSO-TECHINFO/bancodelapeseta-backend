package com.banco.repositories;

import com.banco.entities.EntityType;
import com.banco.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByEntityTypeAndActiveTrue(EntityType entityType);
}
