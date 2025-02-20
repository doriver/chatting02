package com.exercise.chatting02.chatting.presentation.dto.message;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendMessage {
    private String sender;
    private long senderId;
    private String message;
}
