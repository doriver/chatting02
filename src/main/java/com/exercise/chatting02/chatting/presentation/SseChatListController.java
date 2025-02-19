package com.exercise.chatting02.chatting.presentation;

import com.exercise.chatting02.chatting.application.SseChatListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class SseChatListController {

    private final SseChatListService sseChatListService;

    @GetMapping(path = "/sse/chatList", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sseConnect() {
        SseEmitter emitter = new SseEmitter(300_000L); // 300초 타임아웃
        sseChatListService.addEmitter(emitter);
        return emitter;
    }
}
