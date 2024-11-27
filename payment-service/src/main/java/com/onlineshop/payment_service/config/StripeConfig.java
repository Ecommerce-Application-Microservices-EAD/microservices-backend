// src/main/java/com/example/paymentservice/config/StripeConfig.java

package com.onlineshop.payment_service.config;

import com.stripe.Stripe;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class StripeConfig {

    private static final Logger logger = LoggerFactory.getLogger(StripeConfig.class);

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    /**
     * Initializes the Stripe API key.
     */
    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
        logger.info("Stripe API Key initialized successfully");
    }
}
