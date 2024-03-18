package com.banco.repositories;

import com.banco.entities.Transfer;
import com.banco.entities.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    List<Transfer> findAllByPayerAccount(String accountNumber);

    List<Transfer> findAllByStatus(TransferStatus transferStatus);
}
