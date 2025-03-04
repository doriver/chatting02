package com.exercise.chatting02.user.presentation.api;

import com.exercise.chatting02.common.response.ApiResponse;
import com.exercise.chatting02.user.application.UserSignService;
import com.exercise.chatting02.user.presentation.dto.UserSignInRequest;
import com.exercise.chatting02.user.presentation.dto.UserSignUpRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserSignService userSignService;
    /*
        회원가입
     */
    @PostMapping("/sign-up")
    public ApiResponse<Void> signUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        userSignService.registerUser(
                userSignUpRequest.getNickname(), userSignUpRequest.getPassword());
        return ApiResponse.success(null);
    }

    /*
        로그인
     */
    @PostMapping("/sign-in")
    public ApiResponse<String> signIn(@Valid @RequestBody UserSignInRequest userSignInRequest, HttpServletResponse response) {

        String authedNickname = userSignService.authenticateUser(
                userSignInRequest.getNickname(), userSignInRequest.getPassword());

        if (authedNickname == null) {
            return ApiResponse.success("fail");
        }

        Cookie accessCookie = new Cookie("chatting", authedNickname);
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(90 * 60); // 90분 동안 유효
        accessCookie.setPath("/");

        response.addCookie(accessCookie);
        return ApiResponse.success("success");
    }
}
