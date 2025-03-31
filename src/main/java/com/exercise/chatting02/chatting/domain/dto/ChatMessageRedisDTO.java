package com.exercise.chatting02.chatting.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRedisDTO implements Serializable {
    private long senderId;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendedAt;

    public ChatMessageRedisDTO(long senderId, String message, LocalDateTime sendedAt) {
        this.senderId = senderId;
        this.message = message;
        this.sendedAt = sendedAt;
    }
}
