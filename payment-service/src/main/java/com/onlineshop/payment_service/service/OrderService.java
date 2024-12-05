package com.onlineshop.payment_service.service;

// OrderService.java
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.onlineshop.payment_service.dto.OrderDTO;
import com.onlineshop.payment_service.model.Order;
import com.onlineshop.payment_service.repository.OrderRepository;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderDTO orderDTO) {
        logger.info("Validating and creating order for user: {}", orderDTO.getUserId());
        if (orderDTO.getUserId() == null || orderDTO.getUserId().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        Order order = Order.builder()
                .userId(orderDTO.getUserId())
                .items(orderDTO.getItems())
                .status(orderDTO.getStatus())
                .shippingAddress(orderDTO.getShippingAddress())
                .totalAmount(orderDTO.getTotalAmount())
                .build();
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        logger.info("Fetching all orders from repository");
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserId(String userId) {
        logger.info("Fetching orders for user ID: {}", userId);
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(String orderId) {
        logger.info("Fetching order with ID: {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
    }

    public Order updateOrderStatus(String orderId, String status) {
        logger.info("Updating status for order ID: {} to {}", orderId, status);
        Order order = getOrderById(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void cancelOrder(String orderId) {
        logger.info("Cancelling order with ID: {}", orderId);
        Order order = getOrderById(orderId);
        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}