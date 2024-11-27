package com.onlineshop.payment_service.service;

import com.onlineshop.payment_service.model.Cart;
import com.onlineshop.payment_service.model.Item;
import com.onlineshop.payment_service.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private CartRepository cartRepository;

    /**
     * Adds an item to the cart.
     *
     * @param item the item to add
     */
    public void addItem(Item item) {
        String userId = item.getUserId(); // the userId is part of the Item object
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        Cart cart = cartOpt.orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setItems(new ArrayList<>());
            return newCart;
        });

        // Check if item already exists in the cart
        boolean itemExists = cart.getItems().stream()
                .anyMatch(cartItem -> cartItem.getProductId().equals(item.getProductId()));

        if (itemExists) {
            // Update quantity if item exists
            cart.getItems().forEach(cartItem -> {
                if (cartItem.getProductId().equals(item.getProductId())) {
                    cartItem.setQuantity(cartItem.getQuantity() + item.getQuantity());
                }
            });
        } else {
            // Add new item to the cart
            cart.getItems().add(item);
        }

        cartRepository.save(cart);
    }

    /**
     * Retrieves the cart by user ID.
     *
     * @param userId the user ID
     * @return the cart
     */
    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }

    /**
     * Clears the cart for a user.
     *
     * @param userId the user ID
     * @return true if the cart was cleared, false otherwise
     */
    public boolean clearCart(String userId) {
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            cart.getItems().clear();
            cartRepository.save(cart);
            return true;
        }
        return false;
    }

    /**
     * Removes an item from the cart.
     *
     * @param userId    the user ID
     * @param productId the product ID
     * @return a message indicating the result
     */
    public String removeItemFromCart(String userId, String productId) {
        logger.info("Removing item from cart: userId={}, productId={}", userId, productId);
        Optional<Cart> cartOpt = cartRepository.findByUserId(userId);
        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();
            List<Item> items = cart.getItems();
            boolean itemRemoved = items.removeIf(item -> item.getProductId().equals(productId));
            if (itemRemoved) {
                cartRepository.save(cart);
                return "Item removed from cart successfully";
            } else {
                logger.warn("Item not found in cart: productId={}", productId);
                return "Item not found in cart";
            }
        } else {
            logger.warn("Cart not found for user: userId={}", userId);
            return "Cart not found for user";
        }
    }
}
