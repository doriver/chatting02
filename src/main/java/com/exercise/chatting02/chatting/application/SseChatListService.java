package com.exercise.chatting02.chatting.application;

import com.exercise.chatting02.chatting.presentation.dto.response.ChatRoomListResponse;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class SseChatListService {

    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitter.complete());
        emitter.onError((e) -> {
            emitter.complete();
            log.error("sse 채팅목록 에러 : {}", e.getMessage());
        });
        emitters.add(emitter);
    }

    public void sendEventCountUp(String roomId) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("countUp").data(roomId));
            } catch (IOException e) {
                emitter.complete();
            }
        }
    }

    public void sendEventCountDown(String roomId) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("countDown").data(roomId));
            } catch (IOException e) {
                emitter.complete();
            }
        }
    }

    public void sendEventEndRoom(String roomId) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("roomEnd").data(roomId));
            } catch (IOException e) {
                emitter.complete();
            }
        }
    }

    public void sendEventCreationRoom(String chatRoomJson) {

        ChatRoomListResponse chatRoomListResponse = convertFromJson(chatRoomJson);
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("roomCreation").data(chatRoomListResponse));
            } catch (IOException e) {
                emitter.complete();
            }
        }
    }

    private ChatRoomListResponse convertFromJson(String json) {
        try {
            return new ObjectMapper().readValue(json, ChatRoomListResponse.class);
        } catch (Exception e) {
            throw new ExpectedException(ErrorCode.FAIL_JSON_CONVERT);
        }
    }
}
