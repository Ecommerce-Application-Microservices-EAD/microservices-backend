package com.onlineshop.payment_service;

import com.onlineshop.payment_service.model.Cart;
import com.onlineshop.payment_service.model.Item;
import com.onlineshop.payment_service.repository.CartRepository;
import com.onlineshop.payment_service.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart() {
        Cart cart = new Cart("cart123", "user123", new ArrayList<>());
        when(cartRepository.findByUserId("user123")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Item item = new Item("prod123", "Test Item", 1, 10.0, "user123");
        cartService.addItem("user123", item);
        verify(cartRepository, times(1)).save(cart);

        assertEquals(1, cart.getItems().size(), "Cart should contain 1 item after addition");
    }

    @Test
    void testGetCartByUserId() {
        Cart cart = new Cart("cart123", "user123", new ArrayList<>());
        when(cartRepository.findByUserId("user123")).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId("user123");

        assertEquals("cart123", result.getId(), "Cart ID should be 'cart123'");
        verify(cartRepository, times(1)).findByUserId("user123");
    }

    @Test
    void testClearCart() {
        Cart cart = new Cart("cart123", "user123", new ArrayList<>());
        when(cartRepository.findByUserId("user123")).thenReturn(Optional.of(cart));

        boolean result = cartService.clearCart("user123");

        assertEquals(true, result, "Cart should be cleared successfully");
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void testRemoveItemFromCart() {
        Item item = new Item("prod123", "Test Item", 1, 10.0, "user123");
        Cart cart = new Cart("cart123", "user123", new ArrayList<>());
        cart.getItems().add(item);

        when(cartRepository.findByUserId("user123")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        String result = cartService.removeItemFromCart("user123", "prod123");

        assertEquals("Item removed from cart successfully", result, "Message should indicate success");
        verify(cartRepository, times(1)).save(cart);
    }
}
