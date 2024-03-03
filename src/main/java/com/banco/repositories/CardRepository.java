package com.banco.repositories;

import com.amazonaws.services.comprehend.model.Entity;
import com.banco.entities.Card;
import com.banco.entities.Contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("SELECT * FROM card JOIN contract ON card.contract_id = contract.id JOIN entity_contract ec ON ec.contract_id = contract.id JOIN entities e ON ec.entity_id = e.id WHERE e.tax_id = ?1")
    List<Card> findAllCardsFromEntityTaxId(String taxId);

}
