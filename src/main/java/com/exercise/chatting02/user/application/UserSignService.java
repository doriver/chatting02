package com.exercise.chatting02.user.application;

import com.exercise.chatting02.common.exception.ErrorCode;
import com.exercise.chatting02.common.exception.ExpectedException;
import com.exercise.chatting02.springconfig.security.userDetail.MyUserDetails;
import com.exercise.chatting02.user.domain.model.Role;
import com.exercise.chatting02.user.domain.model.User;
import com.exercise.chatting02.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSignService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    /*
        회원가입
        비밀번호 암호화 일부러 안함
     */
    public void registerUser(String nickname, String password) {
        User user = User.builder()
                .nickname(nickname).password(password).role(Role.USER)
                .build();
        try {
            userRepository.save(user);
        } catch (Exception e) {
            throw new ExpectedException(ErrorCode.FAIL_SIGN_UP);
        }
    }

    /*
        로그인
     */
    public String authenticateUser(String nickname, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(nickname, password);

        // username, password에 대한 검증
        // 메서드authenticate가 실행되면 MyUserDetailsService에있는 메서드loadUserByUsername가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        if (authentication == null) return null;

        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
