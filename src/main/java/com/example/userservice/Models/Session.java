package com.example.userservice.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Session extends BaseModel{
    private String token;
    private Date expiredAt;
    @ManyToOne
    private User user;
    @Enumerated(value = EnumType.ORDINAL)
    private SessionStatus sessionStatus;
}
