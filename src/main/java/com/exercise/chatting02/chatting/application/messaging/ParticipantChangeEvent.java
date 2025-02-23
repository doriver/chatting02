package com.exercise.chatting02.chatting.application.messaging;

import com.exercise.chatting02.chatting.presentation.dto.message.ParticipantMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParticipantChangeEvent {
    private final SimpMessagingTemplate messagingTemplate;
    private final StringRedisTemplate strTemplate;

    public void inAndOut(int access, long roomId, long chatterId, String chatterName) {
        ParticipantMessage participantDTO = ParticipantMessage.builder()
                .chatterId(chatterId).chatterName(chatterName).access(access) // 1: 입장,  0: 퇴장
                .build();

        try {
            // 해당 방에 입장,퇴장 알리기
            messagingTemplate.convertAndSend(
                    "/chatRoom/" + roomId + "/door", participantDTO);
        } catch (MessagingException e) {
            // throw new RuntimeException(e);
        }

        try {
            // 채팅방 목록에서 현재인원 증감
            if (access == 1) {
                strTemplate.convertAndSend("participant/up", roomId+"");
            } else if (access == 0) {
                strTemplate.convertAndSend("participant/down", roomId+"");
            }
        } catch (Exception ignored) {        }
    }
}
