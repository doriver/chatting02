package com.exercise.chatting02.user.presentation.view;

import com.exercise.chatting02.common.annotation.CurrentUser;
import com.exercise.chatting02.user.domain.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/view/user")
public class SignViewController {

    @GetMapping("/sign")
    public String signView(@CurrentUser User user, Model model) {
        if (user != null) {
            model.addAttribute("nickname", user.getNickname());
        }
        return "user/sign";
    }
}
