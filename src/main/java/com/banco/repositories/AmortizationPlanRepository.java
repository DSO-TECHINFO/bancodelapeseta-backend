package com.banco.repositories;

import com.banco.entities.AmortizationPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmortizationPlanRepository extends JpaRepository<AmortizationPlan, Long> {
}
