package com.as.crichub.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
    }
 
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.getReasonPhrase());
        response.put("code", status.value());
        response.put("message", "Validation failed");
        response.put("errors", errors);
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleNotReadableException(HttpMessageNotReadableException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.getReasonPhrase());
        response.put("code", status.value());
        response.put("message", "Malformed or invalid request body");
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(DataNotFoundException ex) {
        // Customize the response for the conflict scenario
        HttpStatus status = HttpStatus.NOT_FOUND;
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.getReasonPhrase());
        response.put("code", status.value());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, status);
    }
    @ExceptionHandler(DataAlreadyExistsException.class)
    public ResponseEntity<Object> productAlreadyExistsException(DataAlreadyExistsException ex) {
        
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage(),HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
    	ErrorResponse errorResponse = new ErrorResponse(HttpStatus.CONFLICT, "Duplicate entry",HttpStatus.CONFLICT.value());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        
    }
    
    @ExceptionHandler(DataConflictException.class)
    public ResponseEntity<Object> handleProductConflict(DataConflictException ex) {
        // Customize the response for the conflict scenario
        HttpStatus status = HttpStatus.CONFLICT;
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, status);
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.getReasonPhrase());
        response.put("code", status.value());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, status);
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.getReasonPhrase());
        response.put("code", status.value());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, status);
    }
    @ExceptionHandler(DecryptionException.class)
    public ResponseEntity<Object> handleDecryptionException(DecryptionException ex) {
        // Customize the response for the decryption exception
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.getReasonPhrase());
        response.put("code", status.value());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, status);
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleException(Exception e) {
        e.printStackTrace();
        //return new ResponseEntity<>("An error occurred while processing your request.", HttpStatus.INTERNAL_SERVER_ERROR);
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request.", HttpStatus.NOT_FOUND.value());
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//  
    }
}
