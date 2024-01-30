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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import javax.crypto.SecretKey;
import java.net.http.HttpHeaders;
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

    public AuthService(UserRepository userRepository,
                       SessionRepository sessionRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    public ResponseEntity<UserDTO> login(String email, String password){
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            return null;
        }
        User user1 = user.get();
        if(!bCryptPasswordEncoder.matches(user1.getPassword(),password)){
            return null;
        }
        Map<String, Object> jsonforJWT = new HashMap<>();
        jsonforJWT.put("email", user1.getEmail());
        jsonforJWT.put("roles", user1.getRoles());
        jsonforJWT.put("expiryAt", new Date());
        jsonforJWT.put("createdAt", new Date());

        MacAlgorithm alg = Jwts.SIG.HS256;
        SecretKey key = alg.key().build();

        String token = Jwts.builder().claims(jsonforJWT).signWith(key,alg).compact();

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setUser(user1);
        session.setToken(token);
        session.setExpiredAt(new Date());
        sessionRepository.save(session);

        UserDTO userDTO = user1.from(user1);

        MultiValueMapAdapter<String,String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add("content","auth-token"+token);

        ResponseEntity<UserDTO> responseEntity = new ResponseEntity<>(userDTO, headers, HttpStatus
                .OK);
        return responseEntity;
    }
    public UserDTO signup(String email, String password){
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isEmpty()){
            return null;
        }
        User user1 = new User();
        user1.setEmail(email);
        user1.setPassword(bCryptPasswordEncoder.encode(password));
        User user2 = userRepository.save(user1);
        return user1.from(user2);
    }
    public ResponseEntity<Void> logOut(String token, String email){
        Optional<Session> session = sessionRepository.findByTokenAndUser_Email(token,email);
        if(session.isEmpty()){
            return null;
        }
        Session session1 = session.get();
        session1.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session1);
        return ResponseEntity.ok().build();
    }
    public SessionStatus validateToken(String token, String email){
        Optional<Session> session = sessionRepository.findByTokenAndUser_Email(token,email);
        if(session.isEmpty()){
            return null;
        }

        MacAlgorithm alg = Jwts.SIG.HS256;
        SecretKey key = alg.key().build();

        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        return SessionStatus.ACTIVE;

    }

}
