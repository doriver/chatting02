package com.exercise.chatting02.user.presentation.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/t12")
public class TestController {

    @GetMapping("/log")
    @ResponseBody
    public String logTest() {
        log.error("error from log");
        log.warn("warn from log");
        log.info("info from log");
        log.debug("debug from log");
        log.trace("trace from log");
        return "suc";
    }


    @GetMapping("/check-role")
    @ResponseBody
    public String checkUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "No authentication found";
        }
        String roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));
        return "User Roles: " + roles;
    }
}
