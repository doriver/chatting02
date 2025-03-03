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

    @NotNull
    private String nickname; // 필수값, username역할도 함

    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private Role role;

    @NotNull
    private String password;

    @Builder
    public User(String nickname, String password, Role role) {
        this.nickname = nickname;
        this.password = password;
        this.role = role;
    }

}

