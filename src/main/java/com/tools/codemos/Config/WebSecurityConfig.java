package com.tools.codemos.Config;


import com.tools.codemos.jwt.JwtAccessDeniedHandler;
import com.tools.codemos.jwt.JwtAuthenticationEntryPoint;
import com.tools.codemos.jwt.JwtSecurityConfig;
import com.tools.codemos.jwt.TokenProvider;

import com.tools.codemos.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@Component
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final CustomOAuth2UserService customOAuth2UserService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()   //rest api 할때 csrf끄기.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// 세션사용 x jwt할떄
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()        //로그인 관련 전체접근
                .antMatchers("/leaderboard").permitAll()
                .antMatchers("/leaderboard/**").permitAll()
                .antMatchers("/api/v1/**").permitAll()//리더보드 조회 전체접근
                .antMatchers("/user/**").permitAll()
                .antMatchers("/login/oauth2/code/google").permitAll()
                .anyRequest().authenticated()
                .and()
                .anonymous()
                .authorities("ROLE_USER")
                .and()
                .apply(new JwtSecurityConfig(tokenProvider))
                .and()
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)));

        return http.build();
    }
}
