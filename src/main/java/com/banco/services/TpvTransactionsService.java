package com.banco.services;

import com.banco.entities.TpvTransactions;
import com.banco.exceptions.CustomException;

import java.util.List;

/**
 * <h2>Interface TpvTransactionsService</h2>
 * <p>Interfaz que define el servicio que permite comunicar con el repositorio
 * para obtener los datos relativos a las transacciones asociadas a un tpv.</p>
 */
public interface TpvTransactionsService {
    List<TpvTransactions> getTpvTransactionsInfo() throws CustomException;
}
