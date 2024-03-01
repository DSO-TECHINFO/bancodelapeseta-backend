package com.banco.repositories;

import com.banco.entities.Card;
import com.banco.entities.Contract;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByContract(Contract contract);

}
