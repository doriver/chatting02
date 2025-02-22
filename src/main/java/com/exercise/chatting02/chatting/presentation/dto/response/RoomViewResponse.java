package com.exercise.chatting02.chatting.presentation.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomViewResponse {
    private Long roomId;
    private Long mentorId;
    private String mentor; // 멘토 닉네임
    private String roomName; // 단톡방 주제
    private int userLimit; // 참여 인원 제한

    private List<ParticipantDTO> chatterList;

}
