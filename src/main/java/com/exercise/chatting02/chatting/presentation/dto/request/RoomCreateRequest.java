package com.exercise.chatting02.chatting.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomCreateRequest {
    @NotBlank(message = "단체채팅방 주제를 입력하세요.")
    @Size(min = 1, max = 100, message = "채팅방 주제는 100자 이하로 입력하세요.")
    private String roomName;

    @Min(value = 5, message = "최소 인원은 5명 입니다.")
    @Max(value = 15, message = "최대 인원은 15명 입니다.")
    private int userLimit;
}
