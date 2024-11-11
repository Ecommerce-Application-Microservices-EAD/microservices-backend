package com.onlineshop.user_service;

import com.onlineshop.user_service.client.AuthClient;
import com.onlineshop.user_service.model.User;
import com.onlineshop.user_service.controller.UserController;
import com.onlineshop.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthClient authClient;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    void testRegisterUser() {
        when(userService.registerUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.registerUser(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService, times(1)).registerUser(any(User.class));
    }

    @Test
    void testLoginWithValidCredentials() {
        User foundUser = new User();
        foundUser.setUsername("testuser");
        foundUser.setPassword("encodedPassword"); // Password stored in DB
        foundUser.setRole("USER");

        when(userService.findByUsername(eq("testuser"))).thenReturn(foundUser);
        when(passwordEncoder.matches(eq("password"), eq("encodedPassword"))).thenReturn(true);
        when(authClient.getToken(eq("testuser"), eq("USER"))).thenReturn("testToken");

        ResponseEntity<?> response = userController.login(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testToken", response.getBody());
        verify(authClient, times(1)).getToken(eq("testuser"), anyString());
    }


    @Test
    void testLoginWithInvalidCredentials() {
        when(userService.findByUsername(eq("testuser"))).thenReturn(null);

        ResponseEntity<?> response = userController.login(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
        verify(authClient, never()).getToken(anyString(), anyString());
    }

    @Test
    void testLoginWithTokenGenerationError() {
        User foundUser = new User();
        foundUser.setUsername("testuser");
        foundUser.setPassword("encodedPassword");
        foundUser.setRole("USER");

        when(userService.findByUsername(eq("testuser"))).thenReturn(foundUser);
        when(passwordEncoder.matches(anyString(), eq("encodedPassword"))).thenReturn(true);
        when(authClient.getToken(eq("testuser"), eq("USER"))).thenThrow(new RuntimeException("Token error"));

        ResponseEntity<?> response = userController.login(user);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error while authenticating", response.getBody());
    }
}

