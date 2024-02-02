package com.example.userservice.Controllers;

import com.example.userservice.Models.SessionStatus;
import com.example.userservice.Services.AuthService;
import com.example.userservice.DTOs.*;
import com.example.userservice.Utility.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.example.userservice.Utility.Constants.LoginSuccesseful;
import static com.example.userservice.Utility.Constants.LogoutSuccesseful;

/*
* AuthController will handle authentication and authorization of user
* */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    //User will get token if authenticated successfully
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request){
        ResponseEntity<LoginResponseDTO> responseEntity;
        LoginResponseDTO loginResponseDTO;
        try{
            String token = authService.login(request.getEmail(), request.getPassword());

            loginResponseDTO = new LoginResponseDTO(LoginSuccesseful);
            loginResponseDTO.setEmail(request.getEmail());
            loginResponseDTO.setToken(token);

            MultiValueMapAdapter<String,String> headers = new MultiValueMapAdapter<>(new HashMap<>());
            headers.add("content","auth-token"+token);
            responseEntity = new ResponseEntity<>(loginResponseDTO, headers, HttpStatus.OK);
        }
        catch (Exception e){
            loginResponseDTO = new LoginResponseDTO(e.getMessage());
            responseEntity = new ResponseEntity<>(loginResponseDTO, HttpStatus.OK);
        }
        return responseEntity;
    }

    //Session will be marked as expire if authenticated user logout
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO request){
        ResponseEntity<LogoutResponseDTO> responseEntity;
        LogoutResponseDTO logoutResponseDTO;
        try {
            authService.logOut(request.getToken(), request.getEmail());
            logoutResponseDTO = new LogoutResponseDTO(LogoutSuccesseful);
        }
        catch (Exception ex){
            logoutResponseDTO = new LogoutResponseDTO(ex.getMessage());
            responseEntity = new ResponseEntity<>(logoutResponseDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(logoutResponseDTO, HttpStatus.OK);
    }

    //User need to signup if login for first time
    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signup(@RequestBody SignupRequestDTO request) throws Exception {
        try {
            return new ResponseEntity<>(authService.signup(request.getEmail(),request.getPassword()), HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //Resource services will validate token before processing user request
    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequestDTO request) throws Exception {
        try {
            return new ResponseEntity<>(authService.validateToken(request.getToken(),request.getEmail()),HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
