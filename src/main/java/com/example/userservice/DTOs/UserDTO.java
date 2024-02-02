package com.example.userservice.DTOs;

import com.example.userservice.Models.Role;
import com.example.userservice.Models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDTO {
    private String email;
    private Set<Role> roles = new HashSet<>();

    public static UserDTO from(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setEmail(user.getEmail());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}
