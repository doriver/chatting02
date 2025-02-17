package com.exercise.chatting02.user.domain.repository;

import com.exercise.chatting02.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
