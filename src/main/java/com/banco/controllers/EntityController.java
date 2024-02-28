package com.banco.controllers;

import com.banco.exceptions.CustomException;
import com.banco.services.EntityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/entity")
public class EntityController {
    private final EntityService entityService;
    @GetMapping("")
    public ResponseEntity<?> getEntityInfo() throws CustomException {
        return ResponseEntity.ok(entityService.getEntityInfo());
    }
}
