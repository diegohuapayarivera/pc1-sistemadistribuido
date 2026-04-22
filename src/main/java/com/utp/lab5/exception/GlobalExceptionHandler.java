package com.utp.lab5.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleBusiness(BusinessException ex) {
        return org.springframework.http.ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "error", "BUSINESS_ERROR",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(NotFoundException.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {
        return org.springframework.http.ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "error", "NOT_FOUND",
                        "message", ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {
        return org.springframework.http.ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now().toString(),
                        "error", "INTERNAL_SERVER_ERROR",
                        "message", ex.getMessage()
                ));
    }
}