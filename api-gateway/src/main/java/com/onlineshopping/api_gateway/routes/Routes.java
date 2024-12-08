package com.onlineshopping.api_gateway.routes;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class Routes {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        log.info("Configuring gateway routes...");

        RouteLocator routeLocator = builder.routes()
                .route("product-service", r -> r.path("/api/v1/products/**")
                        .uri("http://product-service:8080")) // Changed to service name
                .route("inventory-service", r -> r.path("/api/inventory/**")
                        .uri("http://inventory-service:8082")) // Update if you have an inventory service
                .route("user-service", r -> r.path("/api/user/**")
                        .uri("http://user-service:8083")) // Changed to service name
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("http://auth-service:8084")) // Changed to service name
                .route("payment-service-payments", r -> r.path("/api/v1/payments/**")
                        .uri("http://payment-service:8085")) // Changed to service name
                .route("payment-service-cart", r -> r.path("/api/v1/cart/**")
                        .uri("http://payment-service:8085")) // Changed to service name
                .route("payment-service-orders", r -> r.path("/api/v1/orders/**")
                        .uri("http://payment-service:8085")) // Changed to service name
                .build();

        log.info("Gateway routes configured successfully.");
        return routeLocator;
    }
}
