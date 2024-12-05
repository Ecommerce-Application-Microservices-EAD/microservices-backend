package com.onlineshop.payment_service.controller;

// OrderController.java
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onlineshop.payment_service.dto.OrderDTO;
import com.onlineshop.payment_service.model.Order;
import com.onlineshop.payment_service.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderDTO) {
        logger.info("Creating order for user: {}", orderDTO.getUserId());
        try {
            Order createdOrder = orderService.createOrder(orderDTO);
            return ResponseEntity.ok(createdOrder);
        } catch (IllegalArgumentException e) {
            logger.error("Error creating order: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error creating order", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.info("Fetching all orders");
        try {
            List<Order> orders = orderService.getAllOrders();
            return ResponseEntity.ok(orders);
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching all orders: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching all orders", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable String userId) {
        logger.info("Fetching orders for user: {}", userId);
        try {
            return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching orders for user: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching orders for user", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable String orderId) {
        logger.info("Fetching order with ID: {}", orderId);
        try {
            return ResponseEntity.ok(orderService.getOrderById(orderId));
        } catch (IllegalArgumentException e) {
            logger.error("Error fetching order by ID: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error fetching order by ID", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable String orderId, @RequestParam String status) {
        logger.info("Updating status for order ID: {} to {}", orderId, status);
        try {
            return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
        } catch (IllegalArgumentException e) {
            logger.error("Error updating order status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error updating order status", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable String orderId) {
        logger.info("Cancelling order with ID: {}", orderId);
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok("Order cancelled successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Error cancelling order: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Unexpected error cancelling order", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
