package com.banco.utils;

import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.backupstorage.model.IllegalArgumentException;
import com.banco.entities.CardNetwork;
import com.banco.exceptions.CustomException;

@Service
public class CardUtils {

    // MII is the first number of a card network
    private final Map<CardNetwork, Integer> MII = Map.of(CardNetwork.AMERICAN_EXPRESS, 3, 
                                                         CardNetwork.VISA, 4,  
                                                         CardNetwork.MASTERCARD, 5);

    private final Map<CardNetwork, Integer> TOTAL_LENGTH = Map.of(CardNetwork.AMERICAN_EXPRESS, 15,
                                                                  CardNetwork.VISA, 16, 
                                                                  CardNetwork.MASTERCARD, 16);

    private static <K, V> K getKeyFromValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null; 
    }

    @Value("${bank.hash.id}")
    private String BANK_HASH_ID;

    public String createCardNumber(CardNetwork cardNetwork, String account, String name, int nthAccountCard) {
        // Seed para la generación de números aleatorios
        long seed = cardNetwork.hashCode() + account.hashCode() + name.hashCode() + (long) nthAccountCard;
        Random random = new Random(seed);
        
        // Genera un número de tarjeta de 16 dígitos
        StringBuilder cardNumber = new StringBuilder();

        int cardLength = TOTAL_LENGTH.get(cardNetwork);
        cardNumber.append(random.nextInt(10));
        for (int i = 1; i < cardLength; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return null;
    }

    public boolean isCardNumberValid(String number) throws CustomException {
        try {
            CardNetwork cardNetwork;
            try {
                cardNetwork = getKeyFromValue(MII, Character.getNumericValue(number.charAt(0)));
            } catch (IllegalArgumentException exception) {
                throw new Exception("MII NOT RECOGNIZED");
            }

            if (number.length() != TOTAL_LENGTH.get(cardNetwork)) 
                throw new Exception("LENGTH NOT VALID");

            int actualValidationNumber = Character.getNumericValue(number.charAt(number.length() - 1));
            int calculatedValidationNumber = generateChecksum(number.substring(0, number.length() - 1));

            if(actualValidationNumber != calculatedValidationNumber)
                throw new Exception("VALIDATION NUMBER ISN'T CORRECT");
        } catch(Exception exception) {
            throw new CustomException("CARD-003", "Card is not valid", 400);
        }
        
        return true;
    }

    private int luhnAlgorithm(String number) {
        int sum = 0;

        boolean alternateFlag = true;

        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            if (alternateFlag) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }

            sum += digit;
            alternateFlag = !alternateFlag;
        }

        return (sum % 10 == 0) ? 0 : 10 - sum % 10;
    }

    private int generateChecksum(String number) {
        return 10 - luhnAlgorithm(number+"0") % 10;
    }
}
