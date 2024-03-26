package com.banco.Factories;

import java.util.Date;

import com.banco.entities.Contract;
import com.banco.entities.Tpv;
import com.banco.utils.EntityUtils;

public class TpvFactory {

    EntityUtils entityUtils;

    public Tpv createTpv() {
        Contract contract = Contract.builder()
            .account(entityUtils.checkIfEntityExists(entityUtils.extractUser()).getContracts().get(0).getContract().getAccount())
            .build();
        Tpv tpv = Tpv.builder()
            .activated(true)
            .contract(contract)
            .activationDate(new Date())
            .tpvCode("xxxx")
            .build();

        contract.setTpv(tpv);
        return tpv;
    }
}
