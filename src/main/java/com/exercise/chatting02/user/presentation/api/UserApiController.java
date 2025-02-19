package com.exercise.chatting02.user.presentation.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
public class UserApiController {

    /*
        Security기반 인증은 추후 적용 예정
     */
    @PostMapping("/sign-in")
    public void signIn() {

    }
}
