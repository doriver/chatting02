package com.exercise.chatting02.user.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname; // 필수값

    @Column(name = "username")
    private String username;

    @Enumerated(EnumType.ORDINAL)
    private Role role;



    @Builder
    public User(String email, String password, String nickname, String phoneNumber, boolean isDeleted, String username) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.username = username;
    }

}

