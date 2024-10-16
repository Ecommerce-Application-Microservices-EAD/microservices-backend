package com.onlineshop.user_service.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface AuthClient {
    @PostExchange("/api/auth/token")
    String getToken(@RequestParam("username") String username, @RequestParam("role") String role);
}
