package com.onlineshopping.api_gateway.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.extern.slf4j.Slf4j;


@Configuration
@EnableWebFluxSecurity
@Slf4j
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    private static final String[] AUTH_WHITELIST = {
            "/v3/api-docs",
            "/api-docs/**",
            "/aggregate/**",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/webjars/**"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers(AUTH_WHITELIST).permitAll()
                        
                        .pathMatchers("/api/auth/**", "/api/user/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/api/v1/products", "/api/v1/products/{productId}", "/api/v1/products/categories", "/api/v1/products/search").permitAll()
                        .pathMatchers(HttpMethod.POST, "/api/v1/products").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/v1/products/{productId}").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/products/{productId}").hasRole("ADMIN")
                        .pathMatchers(HttpMethod.PATCH, "/api/v1/products/{productId}/reduce-quantity").permitAll()
                        
                        .pathMatchers("/api/inventory/**").hasAnyRole("ADMIN")
                        .pathMatchers("/api/order/**").hasAnyRole("USER")
                        .pathMatchers("/api/v1/payments/**").hasAnyRole("USER")
                        .pathMatchers("/api/v1/cart/**").hasAnyRole("USER")
                        
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );

        log.info("SecurityWebFilterChain configured successfully.");
        return http.build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
    }

    private ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }
}