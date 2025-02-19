package com.exercise.chatting02.chatting.presentation;

import com.exercise.chatting02.chatting.application.ChatMessageService;
import com.exercise.chatting02.chatting.presentation.dto.message.RecieveMessage;
import com.exercise.chatting02.chatting.presentation.dto.message.SendMessage;
import com.exercise.chatting02.common.TimeFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@Slf4j
@RequiredArgsConstructor
public class StompChattingController {
    private final ChatMessageService chatMessageService;
    private final TimeFormat timeFormat;

    @MessageMapping("/{roomId}")
    @SendTo("/chatRoom/{roomId}/message")
    public RecieveMessage messageHandler(SendMessage sendMessage
                                , @DestinationVariable("roomId") long roomId) {

        long senderId = sendMessage.getSenderId();
        String sender = sendMessage.getSender();
        String message = sendMessage.getMessage();

        chatMessageService.saveMessage(roomId, senderId, message);

        String formattedSendedAt = timeFormat.hourMinute(LocalDateTime.now());

        RecieveMessage msg = RecieveMessage.builder()
                .content(message).sender(sender).sendedAt(formattedSendedAt)
                .build();

        return msg;
    }
}
