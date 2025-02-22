package com.exercise.chatting02.user.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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


    @Column(name = "nickname")
    @NotNull
    private String nickname; // 필수값

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Role role;

//    @Column(name = "username")
//    private String username;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "password")
//    private String password;

    @Builder
    public User(String nickname) {
        this.nickname = nickname;
    }

}

