package com.exercise.chatting02.xx;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AsyncController {

    private final AsyncService asyncService;

    @GetMapping("/async")
    public String startAsync() {
        // 비동기 작업을 호출하고 즉시 리턴
        asyncService.doLongTask();
        // 필요하다면 future.join() 등을 통해 결과를 동기적으로 받을 수도 있지만,
        // 여기서는 비동기 수행만 확인하고 바로 응답.
        return "비동기 작업이 시작되었습니다!";
    }
}
