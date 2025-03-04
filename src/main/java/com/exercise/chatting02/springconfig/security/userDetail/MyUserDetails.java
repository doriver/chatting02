package com.exercise.chatting02.springconfig.security.userDetail;

import com.exercise.chatting02.user.domain.model.User;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public MyUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }
    
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getNickname();
    }
}
