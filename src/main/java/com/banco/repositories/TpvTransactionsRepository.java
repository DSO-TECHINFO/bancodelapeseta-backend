package com.banco.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.banco.entities.TpvTransactions;

public interface TpvTransactionsRepository extends JpaRepository<TpvTransactions, Long> {
    
}
