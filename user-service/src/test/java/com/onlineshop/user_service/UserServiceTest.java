package com.onlineshop.user_service;

import com.onlineshop.user_service.model.User;
import com.onlineshop.user_service.repository.UserRepository;
import com.onlineshop.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        assertNotNull(registeredUser);
        assertEquals("encodedPassword", registeredUser.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testFindByUsernameNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        User foundUser = userService.findByUsername("nonexistent");

        assertNull(foundUser);
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}
