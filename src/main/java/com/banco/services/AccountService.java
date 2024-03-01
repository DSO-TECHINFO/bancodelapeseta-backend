package com.banco.services;

import com.banco.entities.Contract;
import com.banco.exceptions.CustomException;

import java.util.List;

public interface AccountService {

    public List<Contract> getAccounts() throws CustomException;
}
