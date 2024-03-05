package com.banco.services;

import java.util.ArrayList;
import java.util.List;

import com.banco.dtos.CardDto;
import com.banco.entities.Card;
import com.banco.entities.EntityContract;
import com.banco.exceptions.CustomException;

public interface CardService {
    List<EntityContract> getUserCards() throws CustomException; 
}
