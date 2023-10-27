package com.example.demo.Auth.Controller;

import com.example.demo.Auth.Request.LoginRequest;
import com.example.demo.Auth.Request.RegisterRequest;
import com.example.demo.Auth.Response.AuthenticationResponse;
import com.example.demo.Auth.Service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.CredentialNotFoundException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authorize")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<AuthenticationResponse> authorize(
            @RequestBody LoginRequest request
    ) throws CredentialNotFoundException {
        return ResponseEntity.ok(authenticationService.authorize(request));
    }
}
