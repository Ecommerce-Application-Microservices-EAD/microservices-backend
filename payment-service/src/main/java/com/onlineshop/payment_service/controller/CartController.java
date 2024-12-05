package com.onlineshop.payment_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.payment_service.model.Cart;
import com.onlineshop.payment_service.model.Item;
import com.onlineshop.payment_service.service.CartService;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> addToCart(@PathVariable String userId, @RequestBody Item item) {
        logger.info("Adding item to cart for user {}: {}", userId, item);
        cartService.addItem(userId, item);
        return ResponseEntity.ok("Item added to cart successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable String userId) {
        logger.info("Retrieving cart for user: {}", userId);
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        cartService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared successfully");
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        logger.info("Removing item from cart: userId={}, productId={}", userId, productId);
        String res = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(res);
    }
}
