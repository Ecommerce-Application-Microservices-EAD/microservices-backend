package com.onlineshop.order_service.service;

import com.onlineshop.order_service.client.InventoryClient;
import com.onlineshop.order_service.dto.OrderRequest;
import com.onlineshop.order_service.dto.OrderResponse;
import com.onlineshop.order_service.exception.OrderNotFoundException;
import com.onlineshop.order_service.model.Order;
import com.onlineshop.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

        private final OrderRepository orderRepository;
        private final InventoryClient inventoryClient;

        public OrderResponse placeOrder(OrderRequest orderRequest) {
                // var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(),
                // orderRequest.quantity());
                if ( /* isProductInStock */ true) {
                        Order order = Order.builder()
                                        .orderNumber(UUID.randomUUID().toString())
                                        .skuCode(orderRequest.skuCode())
                                        .price(orderRequest.price())
                                        .quantity(orderRequest.quantity())
                                        .userId(orderRequest.userId())
                                        .status(orderRequest.status())
                                        .build();
                        orderRepository.save(order);
                        log.info("Order placed successfully");
                        return OrderResponse.builder()
                                        .id(order.getId())
                                        .orderNumber(order.getOrderNumber())
                                        .skuCode(order.getSkuCode())
                                        .price(order.getPrice())
                                        .quantity(order.getQuantity())
                                        .userId(orderRequest.userId())
                                        .status(orderRequest.status())
                                        .build();
                }
                log.info("Order failed due to product not in stock");
                throw new RuntimeException("Product " + orderRequest.skuCode() + " not in stock");
        }

        public List<OrderResponse> getOrders() {
                System.out.println("get all orders");
                List<Order> orders = orderRepository.findAll();
                System.out.println("orders: " + orders);
                log.info("Fetching all orders");
                return orders.stream()
                                .map(order -> OrderResponse.builder()
                                                .id(order.getId())
                                                .orderNumber(order.getOrderNumber())
                                                .skuCode(order.getSkuCode())
                                                .price(order.getPrice())
                                                .quantity(order.getQuantity())
                                                .build())
                                .toList();
        }

        public OrderResponse getOrderByOrderId(Long orderId) {
                Order order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new OrderNotFoundException(
                                                "Order with Id " + orderId + " Not Found"));
                return OrderResponse.builder()
                                .id(order.getId())
                                .orderNumber(order.getOrderNumber())
                                .skuCode(order.getSkuCode())
                                .price(order.getPrice())
                                .quantity(order.getQuantity())
                                .userId(order.getUserId())
                                .build();
        }

        public List<OrderResponse> getOrderByUserId(String userId) {
                List<Order> orders = orderRepository.findByUserId(userId);
                if (orders.isEmpty()) {
                        throw new OrderNotFoundException("No Orders found for user with Id " + userId);
                }
                return orders.stream()
                                .map(order -> OrderResponse.builder()
                                                .id(order.getId())
                                                .orderNumber(order.getOrderNumber())
                                                .skuCode(order.getSkuCode())
                                                .price(order.getPrice())
                                                .quantity(order.getQuantity())
                                                .userId(order.getUserId())
                                                .build())
                                .toList();
        }

        public boolean updateOrderStatus(Long orderId, String status) {
                System.out.println("\n\n\n\n status: " + status);
                Optional<Order> orderOptional = orderRepository.findById(orderId);
                if (orderOptional.isPresent()) {
                        Order order = orderOptional.get();
                        order.setStatus(status);
                        // order.setUpdatedAt(new Date());
                        orderRepository.save(order);
                        log.info("Order status updated successfully for orderId: {}", orderId);
                        return true;
                }
                log.warn("Order not found for orderId: {}", orderId);
                return false;
        }

        public boolean modifyOrderStatus(Long orderId, String status) {
                // Additional validation logic can be added here if required.
                log.info("Modifying status for orderId: {}", orderId);
                return updateOrderStatus(orderId, status);
        }
}
