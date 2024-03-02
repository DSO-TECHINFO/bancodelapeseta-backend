package com.banco.controllers;

import com.banco.entities.Entity;
import com.banco.exceptions.CustomException;
import com.banco.services.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/send")
public class NotificationController {

    private final NotificationService notificationService;
    @GetMapping("/email/verification/code")
    public ResponseEntity<?> sendEmailVerificationCode() throws CustomException {

        return ResponseEntity.ok(notificationService.sendEmailVerificationCode());
    }
    @GetMapping("/phone/verification/code")
    public  ResponseEntity<?> sendPhoneVerificationCode() throws CustomException{


        return ResponseEntity.ok(notificationService.sendPhoneVerificationCode());
    }
}
