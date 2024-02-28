package com.banco.controllers;

import com.banco.dtos.TransferInputDto;
import com.banco.services.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/transfers")
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<?> createTransfer(TransferInputDto transferInputDto){
        transferService.createTransfer(transferInputDto);
        return ResponseEntity.ok().build();
    }
}
