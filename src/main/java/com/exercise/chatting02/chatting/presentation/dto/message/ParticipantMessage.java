package com.exercise.chatting02.chatting.presentation.dto.message;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ParticipantMessage {
    private long chatterId;
    private String chatterName;
    private int access; // 1: 입장,  0: 퇴장
}
