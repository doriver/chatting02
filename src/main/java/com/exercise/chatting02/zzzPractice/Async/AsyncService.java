package com.exercise.chatting02.zzzPractice.Async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class AsyncService {

    @Async
    public void doLongTask() {
        log.info("비동기 작업 시작 – Thread: {}", Thread.currentThread().getName());
        try {
            // 예: 7초간 처리 지연
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("비동기 작업 완료 – Thread: {}", Thread.currentThread().getName());
    }
}
