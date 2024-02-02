package com.example.userservice.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private String email;
    private String message;

    public LoginResponseDTO(String message){
        this.message = message;
    }

    LoginResponseDTO(){}
}
