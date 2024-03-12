package com.banco.services;

import com.banco.dtos.TpvDto;
import com.banco.exceptions.CustomException;

import java.util.List;

/**
 * <h2>Interface TpvService</h2>
 * <p>Interfaz que define el servicio TpvService que permite aplicar
 * e implementar la funcionalidad sobre un terminal de punto de venta.</p>
 */
public interface TpvService {
    /**
     * <h2>Method getAll</h2>
     * <p>Obtiene todos los terminales de puntos de venta disponibles.</p>
     * 
     * @return <p>Lista disponible de tpv.</p>
     * @throws CustomException Error al obtener la lista de Tpv.
     */
    List<TpvDto> getAll() throws CustomException;

    /**
     * <h2>Method create</h2>
     * <p>Crea un nuevo con la información recibida.</p>
     * 
     * @param tpv Nuevo Tpv que se va a crear.
     * @throws CustomException Error al crear un nuevo Tpv.
     */
    void create(TpvDto tpv) throws CustomException;

    /**
     * Method return payment
     * Devuelve un pago efectuado
     *
     * @param idTransaction Identificador único de la transacción.
     * @throws CustomException Error al retornar el pago.
     */
    void returnPayment(Long idTransaction) throws CustomException;

    /**
     * <h2>Method activate</h2>
     * <p>Activa el tpv recibido actualizando la fecha de activación.</p>
     * 
     * @param id Identificador único del tpv que se quiere activar.
     * @throws CustomException Error al activar el Tpv.
     */
    void activate(Long id) throws CustomException;

    /**
     * <h2>Method deactivate</h2>
     * <p>Desactiva el tpv recibido actualizando la fecha de desactivación.</p>
     * 
     * @param id Identificador único del tpv que se quiere desactivar.
     * @throws CustomException Error al desactivar el Tpv.
     */
    void deactivate(Long id) throws CustomException;
}