package com.tools.codemos.login;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/home/info").authenticated()
                .antMatchers("/home/admin/**").hasAuthority(UserRole.ADMIN.name())
                .antMatchers("/LB").authenticated() // 로그인된 사용자만 접근 가능
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .usernameParameter("loginId")
                .passwordParameter("password")
                .loginPage("/home/login")
                .defaultSuccessUrl("/home")
                .failureUrl("/home/login")
                .and()
                .logout()
                .logoutUrl("/home/logout")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID");
    }
}