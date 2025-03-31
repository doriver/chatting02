package com.exercise.chatting02.chatting.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    @NotNull
    private ChatRoom room;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    @NotNull
    private ChatParticipant sender;

    @NotNull
    private String message;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime sendAt;

    @Builder
    public ChatMessage(ChatRoom room, ChatParticipant sender, String message, LocalDateTime sendAt) {
        this.room = room;
        this.sender = sender;
        this.message = message;
        this.sendAt = sendAt;
    }
}
