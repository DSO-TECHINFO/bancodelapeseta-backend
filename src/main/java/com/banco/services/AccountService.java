package com.banco.services;

import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;

import java.util.List;

public interface AccountService {

    public List<EntityContract> getAccounts() throws CustomException;
}
