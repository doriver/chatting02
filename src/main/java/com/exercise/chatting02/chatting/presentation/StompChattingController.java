package com.exercise.chatting02.chatting.presentation;

import com.exercise.chatting02.chatting.application.ChatMessageService;
import com.exercise.chatting02.chatting.domain.dto.ChatMessageRedisDTO;
import com.exercise.chatting02.chatting.presentation.dto.message.RecieveMessage;
import com.exercise.chatting02.chatting.presentation.dto.message.SendMessage;
import com.exercise.chatting02.common.TimeFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StompChattingController {
    private final ChatMessageService chatMessageService;

    @MessageMapping("/{roomId}")
    @SendTo("/chatRoom/{roomId}/message")
    public RecieveMessage messageHandler(SendMessage sendMessage
                                , @DestinationVariable("roomId") long roomId) {

        String sender = sendMessage.getSender();
        long senderId = sendMessage.getSenderId();
        String message = sendMessage.getMessage();
        LocalDateTime sendedAt = LocalDateTime.now();

        try {
            chatMessageService.saveMessageRedis(roomId
                    , new ChatMessageRedisDTO(senderId, message, sendedAt));
        } catch(Exception e) {
            log.error("roomId={} 에서 채팅메시지 저장 살패 senderId={}", roomId, senderId);
        }
        TimeFormat timeFormat = new TimeFormat();
        String formattedSendedAt = timeFormat.hourMinute(sendedAt);

        RecieveMessage msg = RecieveMessage.builder()
                .content(message).sender(sender).sendedAt(formattedSendedAt)
                .build();

        return msg;
    }
}
