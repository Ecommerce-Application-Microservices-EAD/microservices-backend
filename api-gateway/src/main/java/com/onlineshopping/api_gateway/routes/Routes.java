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
                                                .uri("http://localhost:8080"))
                                // .route("order-service", r -> r.path("/api/v1/orders/**")
                                //                 .uri("http://localhost:8081"))
                                .route("inventory-service", r -> r.path("/api/inventory/**")
                                                .uri("http://localhost:8082"))
                                .route("user-service", r -> r.path("/api/user/**")
                                                .uri("http://localhost:8083"))
                                .route("auth-service", r -> r.path("/api/auth/**")
                                                .uri("http://localhost:8084"))

                                                
                                .route("payment-service", r -> r.path("/api/v1/payments/**")
                                .uri("http://localhost:8085"))
                                .route("payment-service", r -> r.path("/api/v1/cart/**")
                                .uri("http://localhost:8085"))
                                .route("payment-service", r -> r.path("/api/v1/orders/**")
                                .uri("http://localhost:8085"))
                                
                                // .route("payment-service", r -> r
                                //                 .path("/api/v1/payments/**", "/api/v1/cart/**") // Combine paths
                                //                 .uri("http://localhost:8085"))

                                .build();

                log.info("Gateway routes configured successfully.");
                return routeLocator;
        }
}
