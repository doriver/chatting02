package com.exercise.chatting02.chatting.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessage {
    private String sender;
    private long senderId;
    private String message;
}
