package com.banco.controllers;

import com.banco.dtos.TpvDto;
import com.banco.services.TpvService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    
    @PostMapping("/create")
    public ResponseEntity<?> createTpv(@RequestBody TpvDto tpvDto){
        tpvService.create(tpvDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
