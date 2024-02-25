package com.banco.services;

import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.repositories.EntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EntityServiceImpl implements EntityService{

    private final EntityRepository entityRepository;
    @Override
    public Entity getEntityInfo() throws CustomException {
        return checkIfEntityExists(extractUser());
    }

    private Optional<Entity> extractUser() throws CustomException {
        String userTaxId =  SecurityContextHolder.getContext().getAuthentication().getName();
        return entityRepository.findByTaxId(userTaxId);
    }

    private static Entity checkIfEntityExists(Optional<Entity> userOptional) throws CustomException {
        if(userOptional.isEmpty())
            throw new CustomException("NOTIFICATIONS-002", "User not found", 404);
        return userOptional.get();
    }
}
