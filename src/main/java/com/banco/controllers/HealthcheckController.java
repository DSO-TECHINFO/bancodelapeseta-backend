package com.banco.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/healthcheck")
public class HealthcheckController {

    @GetMapping
    public ResponseEntity<?> healthcheck(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
