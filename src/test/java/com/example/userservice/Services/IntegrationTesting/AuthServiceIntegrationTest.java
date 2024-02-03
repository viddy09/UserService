package com.example.userservice.Services.IntegrationTesting;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.Models.Session;
import com.example.userservice.Models.SessionStatus;
import com.example.userservice.Models.User;
import com.example.userservice.Repositories.SessionRepository;
import com.example.userservice.Repositories.UserRepository;
import com.example.userservice.Services.AuthService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //@Commit
    @Test
    @Transactional
    void testSignUp() throws Exception {
        User user = new User();
        user.setEmail("testSignUp@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("testPassword"));
        UserDTO userDTO = authService.signup(user.getEmail(), user.getPassword());

        assertSame (userDTO.getEmail(), user.getEmail());
    }

    @Test
    @Transactional
    void testValidateToken() throws Exception{
        //New user signup
        UserDTO userDTO = authService.signup("testSignUp1@gmail.com", "TheTestPassword");
        //Getting token
        String token = authService.login(userDTO.getEmail(), "TheTestPassword");
        //Validating token
        try{
            authService.validateToken(token, userDTO.getEmail());
            assert (true);
        }
        catch (Exception e){
            assert (false);
        }
    }

    @Test
    @Transactional
    void testLogin() throws Exception{
        UserDTO userDTO = authService.signup("testSignUp2@gmail.com", "Password");
        //Getting token
        String token = authService.login(userDTO.getEmail(), "Password");
        Optional<Session> session = sessionRepository.findByTokenAndUser_Email(token, userDTO.getEmail());
        assert (session.get().getToken().equals(token));
    }

    @Test
    @Transactional
    void testUserWithWrongPassword() throws Exception{
        UserDTO userDTO = authService.signup("testSignUp3@gmail.com", "Password");
        //Getting token
        try {
            authService.login(userDTO.getEmail(), "ThePassword");
        }
        catch (Exception e){
            assert("Please Enter Correct Password".equals(e.getMessage()));
        }
    }

    @Test
    @Transactional
    void testLogOut() throws Exception{
        UserDTO userDTO = authService.signup("testSignUp4@gmail.com", "Password");
        String token = authService.login(userDTO.getEmail(), "Password");
        authService.logOut(token, userDTO.getEmail());
        Optional<Session> session = sessionRepository.findByTokenAndUser_Email(token, userDTO.getEmail());
        SessionStatus sessionStatus = null;
        if (!session.isEmpty()){
            sessionStatus = session.get().getSessionStatus();
        }
        assertSame(SessionStatus.ENDED,sessionStatus);
    }

    @Test
    @Transactional
    void testInvalidToken() throws Exception{
        //New user signup
        UserDTO userDTO = authService.signup("testSignUp12@gmail.com", "TheTestPassword");
        //Getting token
        String token = authService.login(userDTO.getEmail(), "TheTestPassword");
        //Validating token
        try{
            token += "Invalid";
            authService.validateToken(token, userDTO.getEmail());
            assert (false);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            assertEquals("INVALID Token!!!!",e.getMessage());
        }
    }

}
