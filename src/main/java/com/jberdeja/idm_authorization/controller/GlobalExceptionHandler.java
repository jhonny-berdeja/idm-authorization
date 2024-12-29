package com.jberdeja.idm_authorization.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.jberdeja.idm_authorization.exception.MongoDatabaseIdmException;
import com.jberdeja.idm_authorization.exception.PostgresSqlDatabaseIdmException;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Error message: " + ex.getMessage(), ex);
        var body = buildBadRequestBodyMap(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> errorFields = ex.getBindingResult().getFieldErrors();
        errorFields.forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(PostgresSqlDatabaseIdmException.class)
    public ResponseEntity<?> handlePostgresSqlDatabaseIdmException(PostgresSqlDatabaseIdmException ex) {
        log.error("Error message: " + ex.getMessage(), ex);
        var body = buildInternalServerErrorMap();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(MongoDatabaseIdmException.class)
    public ResponseEntity<?> handleMongoDatabaseIdmException(MongoDatabaseIdmException ex) {
        log.error("Error message: " + ex.getMessage(), ex);
        var body = buildInternalServerErrorMap();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleMongoDatabaseIdmException(Exception ex) {
        log.error("Error message: " + ex.getMessage(), ex);
        var body = buildInternalServerErrorMap();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private Map<String, Object> buildBadRequestBodyMap(String message){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.name());
        body.put("message", message);
        return body;
    }
    private Map<String, Object> buildInternalServerErrorMap(){
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", "An unexpected error occurred. Please try again later.");
        return body;
    }
}