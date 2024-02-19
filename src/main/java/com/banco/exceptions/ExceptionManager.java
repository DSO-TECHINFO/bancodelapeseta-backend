package com.banco.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ExceptionManager {

    @Value("${env}")
    private final String environment;

    public ExceptionManager(@Value("${env}") String environment) {
        this.environment = environment;
    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionHandler(CustomException e, WebRequest request) {
        return setInformation(e, e.getCode(), request, e.getStatusCode());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointerExceptionHandler(NullPointerException e, WebRequest request) {
        return setInformation(e, "Internal server error, please contact support", request, 500);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e, WebRequest request) {
        log.error(e.getMessage());
        return setInformation(e, "UNHANDLED-001", "Internal server error, please contact support", request, 500);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> badCredentialsExceptionHandler(BadCredentialsException e, WebRequest request) {
        return setInformation(e, "BADREQUEST-001", request, 401);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> disabledExcepetionHandler(DisabledException e, WebRequest request) {
        return setInformation(e, "USERS-003", request, 401);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedExceptionHandler(AccessDeniedException e, WebRequest request) {
        return setInformation(e, "ACCESSDENIED-001", request, 403);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e, WebRequest request) {
        return setInformation(e, "DATABASE-001", searchForImportantDataOnQuery(e.getMessage()), request, 404);
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ResponseEntity<?> invalidDataAccessApiUsageExceptionHandler(InvalidDataAccessApiUsageException e, WebRequest request) {
        return setInformation(e, "DATABASE-002", request, 404);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> noSuchElementExceptionController(InvalidDataAccessApiUsageException e, WebRequest request) {
        return setInformation(e, "DATABASE-003", "Not element found with that id", request, 404);
    }

    private ResponseEntity<GlobalException> setInformation(Exception e, String code, String customMessage, WebRequest request, Integer status) {
        Map<String, Object> details = new HashMap<>();
        if (environment.equals("local") || environment.equals("dev"))
            e.printStackTrace();
        details.put("path", request.getContextPath());
        details.put("sessionId", request.getSessionId());
        if(environment.equals("local")||environment.equals("dev"))
            return ResponseEntity.status(status).body(GlobalException.builder().reason(e.getMessage()).code(code).status(status).details(details).build());
        return ResponseEntity.status(status).body(GlobalException.builder().reason(customMessage).code(code).status(status).details(details).build());
    }

    private ResponseEntity<GlobalException> setInformation(Exception e, String code, WebRequest request, Integer status) {
        Map<String, Object> details = new HashMap<>();
        if (environment.equals("local") || environment.equals("dev"))
            e.printStackTrace();
        details.put("path", request.getContextPath());
        details.put("sessionId", request.getSessionId());
        return ResponseEntity.status(status).body(GlobalException.builder().reason(e.getMessage()).code(code).status(status).details(details).build());
    }

    private String searchForImportantDataOnQuery(String data) {
        if (data.contains(".")) {
            String clean = data.split("key")[1];
            String split = clean.split("\\.")[1];
            String field = split.split("'")[0];
            return field.toUpperCase() + " does exists, please choose different one.";
        } else
            return "You cannot do that, please, try again later";
    }
}

