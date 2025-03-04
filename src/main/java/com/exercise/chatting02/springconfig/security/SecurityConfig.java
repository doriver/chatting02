package com.exercise.chatting02.springconfig.security;

import com.exercise.chatting02.springconfig.security.filter.TokenAuthenticationFilter;
import com.exercise.chatting02.springconfig.security.userDetail.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.SecurityContextHolderFilter;

@Configuration
@EnableWebSecurity(debug = true) // 콘솔창에 필터체인등 detail 볼수 있음
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(basic -> basic.disable())
            .csrf(csrf -> csrf.disable())
            .formLogin( form -> form.disable())

            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(requests -> requests
//                    .requestMatchers().permitAll()
//                    .requestMatchers().hasAuthority("MENTOR")
                    .anyRequest().permitAll())
            .addFilterAfter(new TokenAuthenticationFilter(myUserDetailsService)
                    , SecurityContextHolderFilter.class)

        ;
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
