package com.studysmart.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String message;

    public AuthResponse(String token) {
        this.token = token;
        this.message = "Success";
    }
}