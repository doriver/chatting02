package com.exercise.chatting02.chatting.presentation.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantDTO {
    private Long participantId;
    private String nickname;
}
