package com.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenResponseDTO {
    private String message;
    private String tokenStatus;
    public ValidateTokenResponseDTO(String message, String tokenStatus){
        this.message = message;
        this.tokenStatus = tokenStatus;
    }
}
