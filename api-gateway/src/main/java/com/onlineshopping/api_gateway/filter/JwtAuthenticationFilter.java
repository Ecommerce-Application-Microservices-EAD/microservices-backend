package com.onlineshopping.api_gateway.filter;

import com.onlineshopping.api_gateway.dto.TokenValidationResponse;
import com.onlineshopping.api_gateway.dto.TokenValidationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final WebClient.Builder webClientBuilder;
    private final ReactiveJwtDecoder jwtDecoder;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String authServerUrl;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            assert authHeader != null;
            String token = authHeader.substring(7);

            return validateToken(token)
                    .flatMap(isValid -> {
                        if (isValid) {
                            return jwtDecoder.decode(token)
                                    .flatMap(jwt -> {
                                        exchange.getRequest().mutate().header("X-Auth-User-Id", jwt.getSubject());
                                        logJwtRoles(jwt);
                                        return chain.filter(exchange);
                                    });
                        } else {
                            log.warn("Invalid token");
                            return chain.filter(exchange);
                        }
                    })
                    .onErrorResume(e -> {
                        log.error("Error validating token: {}", e.getMessage());
                        return chain.filter(exchange);
                    });
        };
    }

    private Mono<Boolean> validateToken(String token) {
        return webClientBuilder.build()
                .post()
                .uri(authServerUrl + "/validate")
                .bodyValue(new TokenValidationRequest(token))
                .retrieve()
                .bodyToMono(TokenValidationResponse.class)
                .map(TokenValidationResponse::isValid);
    }

    private void logJwtRoles(Jwt jwt) {
        Object rolesClaim = jwt.getClaims().get("role");
        if (rolesClaim != null) {
            log.info("Roles for user: {}", rolesClaim.toString());
        } else {
            log.warn("No 'role' claim found in the JWT");
        }
    }

    public static class Config {
        // Empty class as we don't need any particular configuration
    }
}
