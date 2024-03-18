package com.banco.controllers;

import com.banco.dtos.CancelTransferDto;
import com.banco.dtos.CreateTransferDto;
import com.banco.services.TransferService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/transfers")
@AllArgsConstructor
public class TransferController {
    private TransferService transferService;
    @GetMapping
    public ResponseEntity<?> getAllTransfersFromUser(){
        return ResponseEntity.ok(transferService.listTransfers());
    }

    @PostMapping("/create")
    public  ResponseEntity<?> createTransfer(@RequestBody CreateTransferDto createTransferDto){
        transferService.createTransfer(createTransferDto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelTransfer(@RequestBody CancelTransferDto cancelTransferDto){
        transferService.cancelTransfer(cancelTransferDto);
        return ResponseEntity.ok().build();
    }
}
