package com.exercise.chatting02.user.presentation.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/user")
public class LoginViewController {

    @GetMapping("/login")
    public String loginView() {
        return "user/login";
    }
}
