package com.onlineshopping.api_gateway.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenValidationRequest {
    private String token;

}
