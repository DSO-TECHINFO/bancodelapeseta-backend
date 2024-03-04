package com.banco.repositories;

import com.banco.entities.Card;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("""
        SELECT c 
        FROM Card c 
        JOIN c.contract contract
        JOIN contract.entityContract ec
        JOIN ec.entity e
        WHERE e.taxId = :taxId""")
    List<Card> findAllCardsFromEntityTaxId(String taxId);

}
