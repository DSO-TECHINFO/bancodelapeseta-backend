package com.banco.utils;

import java.util.List;
import java.util.Optional;

import com.banco.entities.Account;
import com.banco.entities.Contract;
import com.banco.entities.Entity;
import com.banco.entities.EntityContract;
import com.banco.entities.EntityContractRole;
import com.banco.exceptions.CustomException;

public class AccountUtils {

    private static EntityContract getEntityContractByAccountNumber(Entity user, String accountNumber) throws CustomException {
        return user.getContracts().stream().filter(entCont -> entCont.getContract().getAccount().getAccountNumber().equals(accountNumber))
                .findFirst().orElseThrow(() -> new CustomException("ACCOUNTS-008", "Account not found", 404));
    }

    public static Account getUserAccountByNumber(Entity user, String accountNumber) {
        return getEntityContractByAccountNumber(user, accountNumber).getContract().getAccount();
    }

    public static Account getAccountIfUserContractRoleIsAllowed(Entity user, String accountNumber, List<EntityContractRole> allowedRoles, CustomException userIsNotAllowedCustomException) throws CustomException {
        EntityContract entityContract = getEntityContractByAccountNumber(user, accountNumber);
        if(!allowedRoles.contains(entityContract.getRole()))
            throw userIsNotAllowedCustomException;
        return entityContract.getContract().getAccount();
    }
}
