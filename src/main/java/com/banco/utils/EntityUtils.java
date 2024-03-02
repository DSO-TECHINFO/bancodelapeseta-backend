package com.banco.utils;

import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EntityUtils {

    private final EntityRepository entityRepository;

    private final CustomException USER_NOT_FOUND = new CustomException("NOTIFICATIONS-002", "User not found", 404);

    public Entity getEntityInfo(String taxId, CustomException entityNotFoundException) throws CustomException {
        Optional<Entity> optionalEntity = entityRepository.findByTaxId(taxId);
        return checkIfEntityExists(optionalEntity, entityNotFoundException);
    }

    public Entity getEntityInfo(String taxId) throws CustomException {
        return getEntityInfo(taxId, USER_NOT_FOUND);
    }

    public Entity getCurrentUserInfo() {
        String userTaxId =  SecurityContextHolder.getContext().getAuthentication().getName();
        return entityRepository.findByTaxId(userTaxId).get();
    }

    public Entity saveEntityInfo(@NonNull Entity entity) {
        return entityRepository.save(entity);
    }

    private Entity checkIfEntityExists(Optional<Entity> userOptional, CustomException entityNotFoundException) throws CustomException {
        if(userOptional.isEmpty())
            throw entityNotFoundException;
        return userOptional.get();
    }
}
