package com.onlineshop.payment_service.service;

import com.onlineshop.payment_service.model.Cart;
import com.onlineshop.payment_service.model.Item;
import com.onlineshop.payment_service.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public void addItem(Item item) {
        String userId = item.getUserId(); //  the userId is part of the Item object
        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setItems(new ArrayList<>());
        }

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

    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }


    public boolean removeItem(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);

        if (cart != null) {
            boolean removed = cart.getItems().removeIf(item -> item.getProductId().equals(productId));

            if (removed) {
                cartRepository.save(cart);
            }
            return removed;
        }

        return false;
    }
    
}
