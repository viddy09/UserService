package com.example.userservice.Security.OAuth_User;

import com.example.userservice.Models.Role;
import org.springframework.security.core.GrantedAuthority;

public class CustomSpringGrantedAuthority implements GrantedAuthority {
    private final Role role;

    public CustomSpringGrantedAuthority(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role.getName();
    }
}
