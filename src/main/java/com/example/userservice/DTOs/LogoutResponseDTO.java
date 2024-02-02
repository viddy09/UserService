package com.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutResponseDTO {
    private String message;
    public LogoutResponseDTO(String message){
        this.message = message;
    }
}
