package com.banco.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.backupstorage.model.IllegalArgumentException;
import com.banco.entities.CardNetwork;
import com.banco.entities.CardType;
import com.banco.exceptions.CustomException;

import lombok.NonNull;

@Service
public class CardUtils {

    // MII is the first number of a card network
    private final Map<CardNetwork, Integer> MII = Map.of(CardNetwork.AMERICAN_EXPRESS, 3, 
                                                         CardNetwork.VISA, 4,  
                                                         CardNetwork.MASTERCARD, 5);

    private final Map<CardNetwork, Integer> TOTAL_LENGTH = Map.of(CardNetwork.AMERICAN_EXPRESS, 15,
                                                                  CardNetwork.VISA, 16, 
                                                                  CardNetwork.MASTERCARD, 16);                                                            

    @Value("${bank.hash.id}")
    private String BANK_HASH_ID;
    
    @Value("${bank.country.code}")
    private String BANK_COUNTRY_CODE;

    private static <K, V> K getKeyFromValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null; 
    }

    public String createCardNumber(@NonNull CardNetwork cardNetwork, @NonNull CardType cardType, @NonNull String account, @NonNull String name, int nthAccountCard) throws CustomException {
        StringBuilder cardNumber = new StringBuilder();
        try {

            // INN
            String innString = generateINN(cardNetwork, cardType, name, account);
            cardNumber.append(innString);

            int cardLength = TOTAL_LENGTH.get(cardNetwork);

            // AccountNumber -> 
            //      INN(length: 6) + 
            //      accountNumber(length: cardLength - innLength - checksumLenght = cardLength - 7) + 
            //      checksum(length: 1)
            int accountNumberLength = cardLength - 7;
            String accountNumberString = generateAccountNumber(account, name, nthAccountCard, accountNumberLength);
            cardNumber.append(accountNumberString);

            // Checksum
            cardNumber.append(generateChecksum(cardNumber.toString()));
                    
        } catch(Exception exception) {
            throw new CustomException("CARD-005", "Card could not be created. Description: " + exception.getMessage(), 500);
        }
        
        return cardNumber.toString();
    }

    private int luhnAlgorithm(String number) {
        int sum = 0;
        boolean alternateFlag = false;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (alternateFlag) {
                digit *= 2;
                if (digit > 9) 
                    digit -= 9;
            }

            sum += digit;
            alternateFlag = !alternateFlag;
        }

        System.out.printf("sum -> %s, number -> %s\n", sum, (sum % 10 == 0) ? 0 : 10 - sum % 10);

        return (sum % 10 == 0) ? 0 : 10 - sum % 10;
    }

    // genera un numero de 6 cifras, el de la network y 5 numeros mas generados segun el banco, la tarjeta 
    private String generateINN(CardNetwork cardNetwork, CardType cardType, String bankId, String bankCountry) throws Exception {
        long seed;
        try {
            seed = cardNetwork.ordinal() + cardType.ordinal() + generateLongFromHash(bankId) + generateLongFromHash(bankCountry);
        } catch (Exception e) {
            throw new Exception("INN Generation failed");
        } 
        Random random = new Random(seed);

        StringBuilder innBuilder = new StringBuilder();

        innBuilder.append(MII.get(cardNetwork));
        innBuilder.append(random.nextInt((int) Math.pow(10,5)));


        System.out.println(innBuilder.toString());
        return innBuilder.toString();
    }

    private String generateAccountNumber(String account, String name, int nthAccountCard, int length) throws Exception {
        long seed;
        try {
            seed = nthAccountCard + generateLongFromHash(account) + generateLongFromHash(name);
        } catch (Exception e) {
            throw new Exception("Account Number Generation failed");
        } 
        Random random = new Random(seed);
        return random.nextInt((int) Math.pow(10,length)) + "";
    }

    private int generateChecksum(String number) {
        System.out.print("Number generation: ");
        return luhnAlgorithm(number+"0");
    }

    public boolean isCardNumberValid(String number) throws CustomException {
        try {
            CardNetwork cardNetwork;
            try {
                cardNetwork = getKeyFromValue(MII, Character.getNumericValue(number.charAt(0)));
            } catch (IllegalArgumentException exception) {
                throw new Exception("MII not recognized");
            }

            if (number.length() != TOTAL_LENGTH.get(cardNetwork)) 
                throw new Exception("Length not valid");
            
            System.out.print("Number validation: ");
            if(luhnAlgorithm(number) != 0)
                throw new Exception("Validation number isn't correct");
        } catch(Exception exception) {
            throw new CustomException("CARD-003", "Card is not valid. Description: " + exception.getMessage(), 400);
        }
        
        return true;
    }

    private static byte[] generateHash(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(input.getBytes());
    }

    private static long convertBytesToNumber(byte[] bytes) {
        long number = 0;
        for (int i = 0; i < bytes.length; i++) {
            number += (long) (bytes[i] & 0xFF) << (8 * i);
        }
        return number;
    }

    private static long generateLongFromHash(String input) throws NoSuchAlgorithmException {
        return convertBytesToNumber(generateHash(input));
    }
}
