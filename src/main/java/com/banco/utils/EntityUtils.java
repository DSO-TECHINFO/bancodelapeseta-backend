package com.banco.utils;

import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;

import lombok.AllArgsConstructor;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EntityUtils {

    private final EntityRepository entityRepository;

    public Entity getEntityInfo(String taxId) throws CustomException {
        Optional<Entity> optionalEntity = entityRepository.findByTaxId(taxId);
        return checkIfEntityExists(optionalEntity);
    }

    public Entity getCurrentUserInfo() throws CustomException {
        return checkIfEntityExists(extractUser());
    }

    public Entity saveEntityInfo(Entity entity) {
        return entityRepository.save(entity);
    }

    private Optional<Entity> extractUser() {
        String userTaxId =  SecurityContextHolder.getContext().getAuthentication().getName();
        return entityRepository.findByTaxId(userTaxId);
    }

    private Entity checkIfEntityExists(Optional<Entity> userOptional) throws CustomException {
        if(userOptional.isEmpty())
            throw new CustomException("NOTIFICATIONS-002", "User not found", 404);
        return userOptional.get();
    }
}
