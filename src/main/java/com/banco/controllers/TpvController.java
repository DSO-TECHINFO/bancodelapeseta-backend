package com.banco.controllers;

import com.banco.dtos.TpvDto;
import com.banco.exceptions.CustomException;
import com.banco.services.TpvService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import com.banco.dtos.TpvDtoCreate;

/**
 * <h2>Controller TpvController</h2>
 * <p>Controlador que manejará los distintos terminales de punto de venta.</p>
 */
@AllArgsConstructor
@RestController
@RequestMapping(path = "/tpv")
public class TpvController {
    private final TpvService tpvService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(tpvService.getAll());
    }
    
    @PostMapping("/create/{accountId}")
    public ResponseEntity<?> createTpv(@PathVariable("accountId") Long accountId, @RequestBody TpvDtoCreate tpvDtoCreate){
        tpvService.create(tpvDtoCreate, accountId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/return/{idTransaction}")
    public ResponseEntity<?> returnPayment(@PathVariable Long idTransaction) throws CustomException {
        tpvService.returnPayment(idTransaction);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("activate/{id}")
    public ResponseEntity<?> activate(@PathVariable Long id) {
        return null;
    }

    @GetMapping("deactivate/{id}")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        return null;
    }
}
