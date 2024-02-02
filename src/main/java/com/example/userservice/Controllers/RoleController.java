package com.example.userservice.Controllers;

import com.example.userservice.DTOs.CreateRoleRequestDto;
import com.example.userservice.Models.Role;
import com.example.userservice.Services.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
* RoleController dealing with the request of creating/modifying/deleting roles
* */
@RestController
@RequestMapping("/roles")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    //New roles creation
    @PostMapping("")
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequestDto request) throws Exception{
        try{
            Role role = roleService.createRole(request.getRole());
            return new ResponseEntity<>(role, HttpStatus.OK);
        }
        catch (Exception e){
            throw new RuntimeException("Role creation unsuccessful");
        }
    }
}
