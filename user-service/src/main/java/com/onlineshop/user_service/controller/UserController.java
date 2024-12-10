package com.onlineshop.user_service.controller;

import com.onlineshop.user_service.client.AuthClient;
import com.onlineshop.user_service.model.User;
import com.onlineshop.user_service.service.UserService;
import com.onlineshop.user_service.model.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthClient authClient;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        log.info("User: {}", user);
        User foundUser = userService.findByUsername(user.getUsername());
        log.info("Found user: {}", foundUser);
        if (foundUser != null && passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            try {
                String token = authClient.getToken(foundUser.getUsername(), foundUser.getRole());
                log.info("Token: {}", token);
                return ResponseEntity.ok(token);
            } catch (Exception e) {
                log.error("Error while getting token", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while authenticating");
            }
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        User foundUser = userService.findByUsername(changePasswordRequest.getUsername());
        if (foundUser != null
                && passwordEncoder.matches(changePasswordRequest.getOldPassword(), foundUser.getPassword())) {
            foundUser.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userService.updateUser(foundUser);
            return ResponseEntity.ok("Password changed successfully");
        }
        return ResponseEntity.badRequest().body("Invalid credentials");
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}