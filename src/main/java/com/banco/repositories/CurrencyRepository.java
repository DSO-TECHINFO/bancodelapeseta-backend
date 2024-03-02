package com.banco.repositories;

import com.banco.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrencyRepository extends JpaRepository<Currency, Long> {
    public Optional<Currency> findByCurrency(String currency);
}
