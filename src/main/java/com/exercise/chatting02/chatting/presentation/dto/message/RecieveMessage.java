package com.exercise.chatting02.chatting.presentation.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecieveMessage {
    private String content;
    private String sender;
    private String sendedAt;

}
