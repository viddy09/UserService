package com.example.userservice.Controllers;

import com.example.userservice.DTOs.SetUserRolesRequestDto;
import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.Services.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
*  UserController dealing with assignment roles to user.
*  Providing user details like user_email , assigned role, etc.
* */
@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable("id") Long userId) throws Exception {
        UserDTO userDto = null;
        try {
            userDto = userService.getUserDetails(userId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    //Assigning roles to user
    @PostMapping("/{id}/roles")
    public ResponseEntity<UserDTO> setUserRoles(@PathVariable("id") Long userId,
                                                @RequestBody SetUserRolesRequestDto request) throws Exception {

        UserDTO userDto = null;
        try {
            userDto = userService.setUserRoles(userId, request.getRoleIds());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


}
