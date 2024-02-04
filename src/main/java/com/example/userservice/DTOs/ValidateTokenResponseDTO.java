package com.example.userservice.DTOs;

import com.example.userservice.Models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ValidateTokenResponseDTO {
    private String message;
    private String tokenStatus;
    private String email;
    private List<Role> roles;
    public ValidateTokenResponseDTO(String message, String tokenStatus){
        this.message = message;
        this.tokenStatus = tokenStatus;
    }
}
