package com.banco.repositories;

import com.banco.entities.TpvTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <h2>Repository TpvTransactionsRepository</h2>
 * <p>Representación de las transacciones de un tpv en el repositorio de datos.</p>
 */
public interface TpvTransactionsRepository extends JpaRepository<TpvTransactions, Long> {

    /**
     * <h2>Method FindAllByTpv</h2>
     * <p>Obtiene las transacciones de un tpv disponibles, recibiendo únicamente
     * las más recientes en caso de que no se reciban los parámetros correctamente.</p>
     *
     * @param tpvId Identificador único del tpv al que pertenecen las transacciones.
     * @param init Inicio del intervalo.
     * @param end Fin del intervalo.
     * @return Lista disponible de transacciones de un tpv específico.
     */
    @Query(value = """
            SELECT
                ts.*
            FROM
                tpv_transactions ts
            WHERE
                ts.tpvId = :tpvId
            ORDER BY
                ts.activation_date DESC
            LIMIT :init,:end
            """)
    List<TpvTransactions> findAllByTpv(
            @Param("tpvId") Long tpvId,
            @Param("init") int init,
            @Param("end") int end
    );

}
