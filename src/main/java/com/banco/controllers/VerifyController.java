package com.banco.controllers;

import com.banco.dtos.EmailPhoneVerificationDto;
import com.banco.dtos.SignTransferRequestDto;
import com.banco.dtos.TransactionVerificationDto;
import com.banco.entities.Transfer;
import com.banco.exceptions.CustomException;
import com.banco.services.VerifyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/verify")
public class VerifyController {
    private final VerifyService verifyService;
    @PostMapping("/email")
    public ResponseEntity<?> verifyEmail(@RequestBody EmailPhoneVerificationDto emailPhoneVerification) throws CustomException {
        verifyService.verifyEmail(emailPhoneVerification);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/phone")
    public ResponseEntity<?> verifyPhone(@RequestBody EmailPhoneVerificationDto emailPhoneVerification) throws CustomException {
        verifyService.verifyPhone(emailPhoneVerification);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/transaction/unsigned")
    public ResponseEntity<?> verifyTransaction(@RequestBody TransactionVerificationDto transactionVerificationDto) throws CustomException {
        return ResponseEntity.ok(verifyService.verifyTransaction(transactionVerificationDto));
    }
    @PostMapping("/transaction/signed")
    public ResponseEntity<?> verifyTransactionWithSign(@RequestBody TransactionVerificationDto transactionVerificationDto) throws CustomException {
        return ResponseEntity.ok(verifyService.verifyTransactionWithSign(transactionVerificationDto));
    }

    @PostMapping("/transfer/sign")
    public ResponseEntity<?> verifyTranferWithSign(@RequestBody SignTransferRequestDto signTransferRequestDto) throws CustomException {
        return ResponseEntity.ok(verifyService.verifyTransferWithSign(signTransferRequestDto));
    }

}
