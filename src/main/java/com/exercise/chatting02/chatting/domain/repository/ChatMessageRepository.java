package com.exercise.chatting02.chatting.domain.repository;

import com.toty.chatting.domain.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
}
