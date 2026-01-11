package com.exercise.chatting02.springconfig.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean(name = "roomEndTaskExecutor")
    public Executor roomEndTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);       // 기본 5명은 항상 대기
        executor.setMaxPoolSize(10);      // 너무 바쁘면 10명까지 늘림
        executor.setQueueCapacity(50);   // 대기실은 50명까지
        executor.setThreadNamePrefix("roomEndAsync-"); // 로그에서 확인용
        executor.initialize();
        return executor;
    }
}
