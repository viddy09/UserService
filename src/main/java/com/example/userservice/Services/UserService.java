package com.example.userservice.Services;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.Models.Role;
import com.example.userservice.Models.User;
import com.example.userservice.Repositories.RoleRepository;
import com.example.userservice.Repositories.UserRepository;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    public UserService(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    public UserDTO getUserDetails(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.map(User::from).orElse(null);

    }

    public UserDTO setUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Role> roles = roleRepository.findAllByIdIn(roleIds);

        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();
        user.setRoles(Set.copyOf(roles));

        User savedUser = userRepository.save(user);

        return User.from(savedUser);
    }
}
