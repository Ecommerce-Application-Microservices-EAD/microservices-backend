package com.onlineshop.auth_service.controller;

import com.onlineshop.auth_service.dto.TokenValidationRequest;
import com.onlineshop.auth_service.dto.TokenValidationResponse;
import com.onlineshop.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final JwtUtil jwtUtil;

    @PostMapping("/token")
    public ResponseEntity<String> createToken(@RequestParam String username, @RequestParam String role) {
        log.info("Received request to create token. Username: {}, Role: {}", username, role);
        try {
            String token = jwtUtil.generateToken(username, role);
            log.info("Generated token: {}", token);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            log.error("Error generating token for user: {}. Exception: {}", username, e.getMessage(), e);
            return ResponseEntity.status(500).body("Token generation failed");
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestBody TokenValidationRequest request) {
        log.info("Validating token: {}", request.getToken());
        boolean isValid = jwtUtil.validateToken(request.getToken());
        if (isValid) {
            String username = jwtUtil.extractUsername(request.getToken());
            return ResponseEntity.ok(new TokenValidationResponse(true, username));
        }
        return ResponseEntity.ok(new TokenValidationResponse(false, null));
    }
}