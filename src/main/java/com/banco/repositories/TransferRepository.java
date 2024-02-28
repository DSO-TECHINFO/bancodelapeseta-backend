package com.banco.repositories;

import com.banco.entities.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository  extends JpaRepository<Transfer, Long> {
}
