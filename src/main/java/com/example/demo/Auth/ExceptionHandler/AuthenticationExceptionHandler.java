package com.example.demo.Auth.ExceptionHandler;

import com.example.demo.Shared.Response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleCredentialNotFoundException(AuthenticationException exception) {
        return ResponseEntity.status(401).body(new ErrorResponse(exception.getMessage()));
    }
}
