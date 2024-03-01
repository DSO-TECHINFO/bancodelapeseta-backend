package com.banco.services;

import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;

public interface EntityService {
    Entity getEntityInfo(String taxId) throws CustomException;
    Entity getCurrentUserInfo() throws CustomException;
    Entity saveEntityInfo(Entity entity) throws CustomException;
}
