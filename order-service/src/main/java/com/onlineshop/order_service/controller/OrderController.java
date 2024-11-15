package com.onlineshop.order_service.controller;

import com.onlineshop.order_service.dto.OrderRequest;
import com.onlineshop.order_service.dto.OrderResponse;
import com.onlineshop.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest) {
       return orderService.placeOrder(orderRequest);
    }

    @GetMapping
    public List<OrderResponse> getOrders() {
        return orderService.getOrders();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrderByOrderId(@PathVariable Long orderId){
        return orderService.getOrderByOrderId(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<OrderResponse> getOrderByUserId(@PathVariable String userId){
        return orderService.getOrderByUserId(userId);
    }
}
