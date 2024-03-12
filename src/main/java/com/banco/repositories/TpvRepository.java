package com.banco.repositories;

import com.banco.entities.Tpv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * <h2>Repository TpvRepository</h2>
 * <p>Representación de un terminal de punto de venta en el repositorio de datos.</p>
 */
public interface TpvRepository extends JpaRepository<Tpv, Long> {
    /**
     * Obtiene el Tpv asociado a un contrato específico.
     * @param contractId Identificador único del contrato cuyo tpv se va a obtener.
     * @return Tpv asociado al contrato recibido
     */
    @Query(value = """
            SELECT
                t.*
            FROM
                tpv t
            WHERE
                t.contract_id = :contractId
            """,
            nativeQuery = true)
    Tpv findByContractId(
            @Param("contractId") Integer contractId
    );
}
