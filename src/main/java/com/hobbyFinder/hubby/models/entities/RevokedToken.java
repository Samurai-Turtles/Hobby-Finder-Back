package com.hobbyFinder.hubby.models.entities;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Table(name = "revoked_tokens")
public class RevokedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Getter
    @Column(unique = true, nullable = false)
    private String token;

    @Column
    private LocalDateTime revokedAt;

    public RevokedToken() {}

    public RevokedToken(String token){
        this.token = token;
        this.revokedAt = LocalDateTime.now();
    }

}
