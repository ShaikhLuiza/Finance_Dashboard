package com.example.finance.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 1. Handles cases where records or users are not found
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Resource error");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    // 2. Handles Access Denied cases for incorrect user roles
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Access Denied");
        body.put("message", "You do not have permission to access this resource.");

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    // 3. Fallback handler to make sure the app never returns an ugly raw stack trace
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneralError(Exception ex, HttpServletRequest request) {
        String uri = request.getRequestURI();

        // Let H2 console and logout handle their own errors natively
        if (uri.startsWith("/h2-console") || uri.startsWith("/logout") || uri.startsWith("/login")) {
            throw new RuntimeException(ex);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("error", "Internal Server Error");
        body.put("message", "Something went wrong. Please check your input.");

        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}