package com.example.userservice.Services;

import com.example.userservice.DTOs.UserDTO;
import com.example.userservice.Models.Session;
import com.example.userservice.Models.SessionStatus;
import com.example.userservice.Models.User;
import com.example.userservice.Repositories.SessionRepository;
import com.example.userservice.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Getter
@Setter
public class AuthService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //JWT sign algo and key
    private MacAlgorithm alg = Jwts.SIG.HS256;
    private SecretKey key = alg.key().build();

    public AuthService(UserRepository userRepository,
                       SessionRepository sessionRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //User authentication and providing token to user
    public String login(String email, String password) throws Exception{
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new Exception("You need to SignUp before Login");
        }

        //Authentication using Bcrypt
        User user1 = user.get();
        if(!bCryptPasswordEncoder.matches(password,user1.getPassword())){
            throw new Exception("Please Enter Correct Password");
        }

        //Token generation
        String token = this.getToken(user1);

        //Create Session
        this.createSessionDetails(user1, token);

        return token;
    }

    private String getToken(User user){
        //PayLoad of token
        Map<String, Object> jsonforJWT = new HashMap<>();
        jsonforJWT.put("email", user.getEmail());
        //jsonforJWT.put("roles", user.getRoles());
        jsonforJWT.put("expiryAt", new Date());
        jsonforJWT.put("createdAt", new Date());

        //Token generation
        String token = Jwts.builder().claims(jsonforJWT).signWith(this.key,this.alg).compact();

        return token;
    }

    private void createSessionDetails(User user, String token){
        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setUser(user);
        session.setToken(token);
        session.setExpiredAt(new Date());
        sessionRepository.save(session);
    }

    public UserDTO signup(String email, String password) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isEmpty()){
            throw new Exception("User is already registered");
        }

        //Create new user
        User user1 = new User();
        user1.setEmail(email);
        //Password Encrypted using BCrypt
        user1.setPassword(bCryptPasswordEncoder.encode(password));
        User user2 = userRepository.save(user1);
        return user1.from(user2);
    }


    public void logOut(String token, String email) throws Exception{
        Optional<Session> session = sessionRepository.findByTokenAndUser_Email(token,email);
        if(session.isEmpty()){
            throw new Exception("UnAuthorized Request");
        }
        Session session1 = session.get();
        session1.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session1);
    }

    public SessionStatus validateToken(String token, String email) throws Exception {
        Optional<Session> session = sessionRepository.findByTokenAndUser_Email(token,email);
        if(session.isEmpty()){
            throw new Exception("Something went wrong. Try login in again");
        }

        Claims claims = Jwts.parser().verifyWith(this.key).build().parseSignedClaims(token).getPayload();

         /*if(exiprytime > currentdate) {

         }*/
        return SessionStatus.ACTIVE;

    }

}
