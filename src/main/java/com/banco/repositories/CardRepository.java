package com.banco.repositories;

import com.banco.entities.Card;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String string);
}
