package com.example.skill_sharing_backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception e) {
        logger.error("Unexpected error occurred: ", e);
        Map<String, String> response = new HashMap<>();
        response.put("error", "An unexpected error occurred");
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException e) {
        logger.error("Entity not found: ", e);
        Map<String, String> response = new HashMap<>();
        response.put("error", "Not Found");
        response.put("message", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
} 