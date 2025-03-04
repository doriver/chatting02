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
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

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
            .logout(logout -> logout
                    .logoutUrl("/user/sign-out")
                    .deleteCookies("chatting")
                    .logoutSuccessUrl("/view/user/sign")
            )
            .addFilterAfter(new TokenAuthenticationFilter(myUserDetailsService)
                    , SecurityContextHolderFilter.class)

        ;
        return http.build();
    }

    @Bean // cookie헤더의 value값이 한글일때 에러났었는데 이거 해주니까 해결됨
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        Pattern allowed = Pattern.compile("[\\p{IsAssigned}&&[^\\p{IsControl}]]*");
        firewall.setAllowedHeaderValues((header) -> {
            String parsed = new String(header.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            return allowed.matcher(parsed).matches();
        });
        return firewall;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
