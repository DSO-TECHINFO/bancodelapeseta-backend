package com.banco.services;

import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;

public interface EntityService {
    Entity getEntityInfo() throws CustomException;
}
