package com.exercise.chatting02.user.presentation.api;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/user")
public class UserApiController {

    /*
        로그인
     */
    @PostMapping("/sign-in")
    public void signIn(HttpServletResponse response) {
        Cookie accessCookie = new Cookie("chatting", null);
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(90 * 60); // 90분 동안 유효
        accessCookie.setPath("/");

        response.addCookie(accessCookie);
    }
}
