package com.exercise.chatting02.chatting.presentation;

import com.exercise.chatting02.chatting.application.messaging.SseChatListService;
import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SseChatListController {

    private final SseChatListService sseChatListService;

    @GetMapping(path = "/sse/chatList", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sseConnect() {
        SseEmitter emitter = new SseEmitter(300_000L); // 300초 타임아웃
        sseChatListService.addEmitter(emitter);
        try { // 만료시간이 되면 브라우저에서 자동으로 서버에 재연결 요청, Emitter를 생성하고 나서 만료 시간까지 아무런 데이터도 보내지 않으면 재연결 요청시 503 Service Unavailable 에러가 발생할 수 있다.
            emitter.send(SseEmitter.event().name("connect").data("connected!"));
        } catch (IOException e) {
            emitter.complete();
            throw new RuntimeException(e);
        }
        return emitter;
    }

    @ExceptionHandler(Exception.class)
    public void handleSseException(Exception e) {
        
        if (e instanceof AsyncRequestTimeoutException) {
            log.info("SseEmitter 타임아웃, AsyncRequestTimeoutException :{}", e.getMessage());
        }
        else if (e instanceof  IOException) {
            if (e.getMessage().equals("현재 연결은 사용자의 호스트 시스템의 소프트웨어의 의해 중단되었습니다")) {
            } else {
                log.error("SSE 예외 발생 ", e);
            }
        } else {
            log.error("SSE 예외 발생 ", e);
        }
    }
}
