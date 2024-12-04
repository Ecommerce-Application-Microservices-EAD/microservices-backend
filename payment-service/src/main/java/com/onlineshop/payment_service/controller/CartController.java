package com.onlineshop.payment_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onlineshop.payment_service.model.Cart;
import com.onlineshop.payment_service.model.Item;
import com.onlineshop.payment_service.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v1/cart")
@CrossOrigin(origins = "http://localhost:3000")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    private CartService cartService;

    /**
     * Adds an item to the cart.
     *
     * @param item the item to add
     * @return the response entity with a success message
     */
    @PostMapping("/{userId}")
    public ResponseEntity<String> addToCart(@PathVariable String userId, @RequestBody Item item) {
        logger.info("Adding item to cart for user {}: {}", userId, item);
        cartService.addItem(userId, item);
        return ResponseEntity.ok("Item added to cart successfully");
    }
    // @PostMapping("/add")
    // public ResponseEntity<String> addToCart(@RequestBody Item item) {
    // logger.info("Adding item to cart: {}", item);
    // cartService.addItem(item);
    // return ResponseEntity.ok("Item added to cart successfully");
    // }

    /**
     * Retrieves the cart by user ID.
     *
     * @param userId the user ID
     * @return the response entity with the cart
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCartByUserId(@PathVariable String userId) {
        logger.info("Retrieving cart for user: {}", userId);
        Cart cart = cartService.getCartByUserId(userId);
        if (cart != null) {
            return ResponseEntity.ok(cart);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Clears the cart for a user.
     *
     * @param userId the user ID
     * @return the response entity with a success or error message
     */

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable String userId) {
        boolean cleared = cartService.clearCart(userId);
        if (cleared) {
            return ResponseEntity.ok("Cart cleared successfully");
        } else {
            return ResponseEntity.status(404).body("Cart not found for user");
        }
    }

    // @DeleteMapping("/clear")
    // public ResponseEntity<String> clearCart(@RequestParam String userId) {
    // boolean cleared = cartService.clearCart(userId);
    // if (cleared) {
    // return ResponseEntity.ok("Cart cleared successfully");
    // } else {
    // return ResponseEntity.status(404).body("Cart not found for user");
    // }
    // }

    /**
     * Removes an item from the cart.
     *
     * @param productId the product ID
     * @param userId    the user ID
     * @return the response entity with a success or error message
     */

    // @DeleteMapping("/remove/{productId}")
    // public ResponseEntity<String> removeFromCart(@PathVariable String productId,
    // @RequestParam String userId) {
    // logger.info("Removing item from cart: productId={}, userId={}", productId,
    // userId);
    // String res = cartService.removeItemFromCart(userId, productId);

    // switch (res) {
    // case "Item removed from cart successfully":
    // return ResponseEntity.ok("Item removed from cart successfully");
    // case "Item not found in cart":
    // return ResponseEntity.status(404).body("Item not found in cart");
    // default:
    // return ResponseEntity.status(404).body("Cart not found for user");
    // }
    // }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<String> removeFromCart(@PathVariable String userId, @PathVariable String productId) {
        logger.info("Removing item from cart: userId={}, productId={}", userId, productId);
        String res = cartService.removeItemFromCart(userId, productId);

        switch (res) {
            case "Item removed from cart successfully":
                return ResponseEntity.ok("Item removed from cart successfully");
            case "Item not found in cart":
                return ResponseEntity.status(404).body("Item not found in cart");
            default:
                return ResponseEntity.status(404).body("Cart not found for user");
        }
    }
}
