package com.banco.utils;

import java.util.List;

import com.banco.entities.Account;
import com.banco.entities.Entity;
import com.banco.entities.EntityContract;
import com.banco.entities.EntityContractRole;
import com.banco.exceptions.CustomException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AccountUtils {
    @Value("${bank.code}")
    public String bankCode;
    @Value("${office.code}")
    public String officeCode;
    @Value("${country.code}")
    public String countryCode;
    @Value("${country.weight}")
    private String countryWeight;

    public void checkAccountNumberValidation(String accountNumber){
        if(accountNumber.length() != 24)
            throw new CustomException("ACCOUNTSVALIDATION-001", "Account number length is invalid", 400);
        String countryCode = accountNumber.substring(0,2);
        String checksum = accountNumber.substring(2,4);
        String bankCode = accountNumber.substring(4,8);
        String officeCode = accountNumber.substring(8,12);
        String controlDigit = accountNumber.substring(12,14);
        String accountNumberClient = accountNumber.substring(14);
        int firstControlDigit = getFirstControlDigit(bankCode, officeCode);
        int secondControlDigit = getSecondControlDigit(accountNumberClient);
        if(!countryCode.equals(this.countryCode))
            throw new CustomException("ACCOUNTSVALIDATION-002", "Account number is invalid", 400);
        if(!(String.valueOf(firstControlDigit) + secondControlDigit).equals(controlDigit))
            throw new CustomException("ACCOUNTSVALIDATION-003", "Account number is invalid", 400);
        if(!getCompleteAccountNumber(accountNumber.substring(4)).equals(accountNumber.substring(2)))
            throw new CustomException("ACCOUNTSVALIDATION-004", "Account number is invalid", 400);
    }
    public String createAccountNumber(){
        String randomAccNumber = RandomStringUtils.randomNumeric(10);
        String accountNumber = bankCode + officeCode + getFirstControlDigit(bankCode, officeCode) + getSecondControlDigit(randomAccNumber) + randomAccNumber;
        String iban = countryCode + getCompleteAccountNumber(accountNumber);
        checkAccountNumberValidation(iban);
        return iban;
    }

    private String getCompleteAccountNumber(String accountNumber){
        {
            String totalAccNumber = accountNumber + countryWeight;
            int m = 0;
            for (var i = 0; i < totalAccNumber.length(); ++i) {
                m = (m * 10 + Integer.parseInt(String.valueOf(totalAccNumber.charAt(i)))) % 97;
            }
            m = 98-m;
            if(m<10){
                return "0" + m + accountNumber;
            }
            return m + accountNumber;
        }
    }
    private static int getFirstControlDigit(String bankCode, String officeCode) {
        int firstControlDigit =(Integer.parseInt(bankCode.substring(0,1)) * 4 +
                Integer.parseInt(bankCode.substring(1,2)) * 8 +
                Integer.parseInt(bankCode.substring(2,3)) * 5 +
                Integer.parseInt(bankCode.substring(3,4)) * 10 +
                Integer.parseInt(officeCode.substring(0,1)) * 9 +
                Integer.parseInt(officeCode.substring(1,2)) * 7 +
                Integer.parseInt(officeCode.substring(2,3)) * 3 +
                Integer.parseInt(officeCode.substring(3,4)) * 6) % 11;
        int resultado = 11 - firstControlDigit;
        if(resultado == 10) resultado = 1;
        if(resultado == 11) resultado = 0;
        return resultado;
    }

    private static int getSecondControlDigit(String accountNumberClient) {
        int secondControlDigit = (Integer.parseInt(accountNumberClient.substring(0,1)) +
                Integer.parseInt(accountNumberClient.substring(1,2)) * 2 +
                Integer.parseInt(accountNumberClient.substring(2,3)) * 4 +
                Integer.parseInt(accountNumberClient.substring(3,4)) * 8 +
                Integer.parseInt(accountNumberClient.substring(4,5)) * 5 +
                Integer.parseInt(accountNumberClient.substring(5,6)) * 10 +
                Integer.parseInt(accountNumberClient.substring(6,7)) * 9 +
                Integer.parseInt(accountNumberClient.substring(7,8)) * 7 +
                Integer.parseInt(accountNumberClient.substring(8,9)) * 3 +
                Integer.parseInt(accountNumberClient.substring(9,10)) * 6) % 11;
        int resultado = 11 - secondControlDigit;
        if(resultado == 10) resultado = 1;
        if(resultado == 11) resultado = 0;
        return resultado;
    }

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
