package com.exercise.chatting02.chatting.application.messaging;

import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.presentation.dto.response.ChatRoomInfoResponse;
import com.exercise.chatting02.common.ToDto;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoomChangeEvent {
    private final SimpMessagingTemplate messagingTemplate;
    private final StringRedisTemplate strTemplate;

    private final ToDto toDto;

    /*
       채팅방 목록 화면에 생성된 채팅방 추가
     */
    public void roomCreation(ChatRoom createdRoom) {
        ChatRoomInfoResponse chatRoomInfoResponse = toDto.chatRoomEntityToDto(createdRoom);
        String message = convertToJson(chatRoomInfoResponse);
        strTemplate.convertAndSend("room/creation", message);
    }

    private String convertToJson(ChatRoomInfoResponse chatRoomInfoResponse) {
        try {
            return new ObjectMapper().writeValueAsString(chatRoomInfoResponse);
        } catch (Exception e) {
            throw new ExpectedException(ErrorCode.FAIL_JSON_CONVERT);
        }
    }
}
