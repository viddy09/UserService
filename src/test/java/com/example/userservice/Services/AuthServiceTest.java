package com.example.userservice.Services;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.Models.Session;
import com.example.userservice.Models.SessionStatus;
import com.example.userservice.Models.User;
import com.example.userservice.Repositories.SessionRepository;
import com.example.userservice.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringBootTest
public class AuthServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SessionRepository sessionRepository;

    @Autowired
    AuthService authService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    //@Commit
    @Test
    void testSignUp() throws Exception {
        User user = new User();
        user.setEmail("testSignUp@gmail.com");
        user.setPassword(bCryptPasswordEncoder.encode("testPassword"));
        UserDTO userDTO = authService.signup(user.getEmail(), user.getPassword());

        assertNotEquals (userDTO.getEmail(), user.getEmail());
    }

    @Test
    void testValidateToken() throws Exception{
        //New user signup
        UserDTO userDTO = authService.signup("testSignUp1@gmail.com", "TheTestPassword");
        //Getting token
        String token = authService.login(userDTO.getEmail(), "TheTestPassword");
        //Validating token
        SessionStatus sessionStatus =  authService.validateToken(token, userDTO.getEmail());
        assert(sessionStatus.equals(SessionStatus.ACTIVE));
    }

    @Test
    void testLogin() throws Exception{
        UserDTO userDTO = authService.signup("testSignUp2@gmail.com", "Password");
        //Getting token
        String token = authService.login(userDTO.getEmail(), "Password");
        Optional<Session> session = sessionRepository.findByTokenAndUser_Email(token, userDTO.getEmail());
        assert (session.get().getToken().equals(token));
    }

    @Test
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
}
