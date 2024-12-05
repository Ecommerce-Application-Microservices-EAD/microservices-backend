package com.onlineshop.payment_service;

import com.onlineshop.payment_service.controller.CartController;
import com.onlineshop.payment_service.model.Cart;
import com.onlineshop.payment_service.model.Item;
import com.onlineshop.payment_service.service.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    void testAddToCart() throws Exception {
        String requestBody = "{\"productId\": \"prod123\", \"name\": \"Test Item\", \"quantity\": 1, \"price\": 10.0, \"userId\": \"user123\"}";

        var result = mockMvc.perform(post("/api/v1/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Item added to cart successfully", result.getResponse().getContentAsString());
    }

    @Test
    void testGetCartByUserId() throws Exception {
        List<Item> items = new ArrayList<>();
        items.add(new Item("prod123", "Test Item", 1, 10.0, "user123"));
        Cart cart = new Cart("cart123", "user123", items);
        Mockito.when(cartService.getCartByUserId("user123")).thenReturn(cart);

        var result = mockMvc.perform(get("/api/v1/cart/user123"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("{\"id\":\"cart123\",\"userId\":\"user123\",\"items\":[{\"productId\":\"prod123\",\"name\":\"Test Item\",\"quantity\":1,\"price\":10.0,\"userId\":\"user123\"}]}",
                result.getResponse().getContentAsString());
    }

    @Test
    void testClearCart() throws Exception {
        Mockito.when(cartService.clearCart("user123")).thenReturn(true);

        var result = mockMvc.perform(delete("/api/v1/cart/clear")
                        .param("userId", "user123"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Cart cleared successfully", result.getResponse().getContentAsString());
    }

    @Test
    void testRemoveFromCart() throws Exception {
        Mockito.when(cartService.removeItemFromCart("user123", "prod123"))
                .thenReturn("Item removed from cart successfully");

        var result = mockMvc.perform(delete("/api/v1/cart/remove/prod123")
                        .param("userId", "user123"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals("Item removed from cart successfully", result.getResponse().getContentAsString());
    }
}
