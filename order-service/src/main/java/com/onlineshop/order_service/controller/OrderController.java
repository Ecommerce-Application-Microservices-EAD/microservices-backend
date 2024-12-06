package com.onlineshop.order_service.controller;

import com.onlineshop.order_service.dto.OrderRequest;
import com.onlineshop.order_service.dto.OrderResponse;
import com.onlineshop.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest) {
//        return orderService.placeOrder(orderRequest);
//    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<OrderResponse> placeOrders(@RequestBody List<OrderRequest> orderRequests) {
        return orderService.placeOrder(orderRequests);
    }

    @GetMapping
    public List<OrderResponse> getOrders() {
        System.out.println("order");
        return orderService.getOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderByOrderId(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrderByUserId(@PathVariable String userId) {
        return orderService.getOrderByUserId(userId);
    }

    @PostMapping("/{orderId}/status")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long orderId, @RequestBody String status) {
        boolean isUpdated = orderService.updateOrderStatus(orderId, status);
        if (isUpdated) {
            return ResponseEntity.ok("Order status updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update order status.");
        }
    }

    @PutMapping("/{orderId}/status/{status}")
    public ResponseEntity<String> modifyOrderStatus(@PathVariable Long orderId, @PathVariable String status) {
        boolean isModified = orderService.modifyOrderStatus(orderId, status);
        if (isModified) {
            return ResponseEntity.ok("Order status modified successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to modify order status.");
        }
    }
}
