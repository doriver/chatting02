package com.exercise.chatting02.chatting.application.messaging;

import com.exercise.chatting02.chatting.domain.model.ChatRoom;
import com.exercise.chatting02.chatting.presentation.dto.response.ChatRoomInfoResponse;
import com.exercise.chatting02.common.utils.ToDto;
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

    public void roomCreation(ChatRoom createdRoom) {
        ChatRoomInfoResponse chatRoomInfoResponse = toDto.chatRoomEntityToDto(createdRoom);
        String message = convertToJson(chatRoomInfoResponse);
        // 채팅방 목록 화면에 생성된 채팅방 추가
        strTemplate.convertAndSend("room/creation", message);
    }

    private String convertToJson(ChatRoomInfoResponse chatRoomInfoResponse) {
        try {
            return new ObjectMapper().writeValueAsString(chatRoomInfoResponse);
        } catch (Exception e) {
            throw new ExpectedException(ErrorCode.FAIL_JSON_CONVERT);
        }
    }

    public void roomEnd(Long roomId) {
        // 해당 방 종료 알리기( 채팅방에 연결된 웹소켓 통신 종료시키기 )
        String destination = "/chatRoom/" + roomId + "/door";
        messagingTemplate.convertAndSend(destination, "DISCONNECT");

        // 채팅방 목록에서 해당방 삭제
        strTemplate.convertAndSend("room/end", roomId+"");
    }
}
