package com.onlineshop.payment_service.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.onlineshop.payment_service.exception.ResourceNotFoundException;
import com.onlineshop.payment_service.model.Order;
import com.onlineshop.payment_service.repository.OrderRepository;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        if (order == null || !StringUtils.hasText(order.getUserId())) {
            throw new IllegalArgumentException("Order or User ID must not be null or empty");
        }
        order.setOrderId(null); // Let MongoDB generate the ID
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUserId(String userId) {
        logger.info("Fetching orders for user id: " + userId);
        if (!StringUtils.hasText(userId)) {
            logger.error("User ID must not be null or empty");
            throw new IllegalArgumentException("User ID must not be null or empty");
        }
        try {
            List<Order> orders = orderRepository.findByUserId(userId);
            logger.info("Successfully fetched orders for user id: " + userId);
            return orders;
        } catch (Exception e) {
            logger.error("Error fetching orders for user id: " + userId, e);
            throw new RuntimeException("Error fetching orders for user id: " + userId, e);
        }
    }

    public Order getOrderById(String orderId) {
        if (!StringUtils.hasText(orderId)) {
            throw new IllegalArgumentException("Order ID must not be null or empty");
        }
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    public Order updateOrderStatus(String orderId, String status) {
        if (!StringUtils.hasText(orderId) || !StringUtils.hasText(status)) {
            throw new IllegalArgumentException("Order ID and status must not be null or empty");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void cancelOrder(String orderId) {
        if (!StringUtils.hasText(orderId)) {
            throw new IllegalArgumentException("Order ID must not be null or empty");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setStatus("cancelled");
        orderRepository.save(order);
    }
}
