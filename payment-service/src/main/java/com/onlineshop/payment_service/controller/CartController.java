package com.onlineshop.payment_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.payment_service.model.Cart;
import com.onlineshop.payment_service.model.Item;
import com.onlineshop.payment_service.service.CartService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody Item item) {
        // System.out.println("Item: " + item);
        cartService.addItem(item);
        return ResponseEntity.ok("Item added to cart successfully");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable String userId) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam String userId) {
        boolean cleared = cartService.clearCart(userId);
        if (cleared) {
            return ResponseEntity.ok("Cart cleared successfully");
        } else {
            return ResponseEntity.status(404).body("Cart not found for user");
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable String productId, @RequestParam String userId) {
        System.out.println("productId: " + productId + " userId: " + userId);
        String res = cartService.removeItemFromCart(userId, productId);

        if (res.equals("Item removed from cart successfully")) {
            return ResponseEntity.ok("Item removed from cart successfully");
        } else if (res.equals("Item not found in cart")) {
            return ResponseEntity.status(404).body("Item not found in cart");
        } else {
            return ResponseEntity.status(404).body("Cart not found for user");
        }
    }

}
