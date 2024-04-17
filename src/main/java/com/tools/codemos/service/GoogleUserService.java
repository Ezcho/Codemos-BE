package com.tools.codemos.service;

import com.tools.codemos.dto.TokenDTO;
import com.tools.codemos.jwt.TokenProvider;
import com.tools.codemos.login.Authority;
import com.tools.codemos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class GoogleUserService {

    private final UserRepository userRepository;
    private final CustomUserDetailsService customUserDetailsService;


    private final TokenProvider tokenProvider;


    @Autowired
    public GoogleUserService(UserRepository userRepository, CustomUserDetailsService customUserDetailsService,
                              TokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenProvider = tokenProvider;
    }

    public TokenDTO registerOrUpdateGoogleUser(String email, String name) {
        System.out.println("registerOrUpdateGoogleUser");
        com.tools.codemos.model.User user = userRepository.findByEmail(email)
                .orElseGet(() -> new com.tools.codemos.model.User());
        user.setEmail(email);
        user.setNickname(name);
        user.setAuthority(Authority.ROLE_USER);
        userRepository.save(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        TokenDTO dto =  tokenProvider.generateTokenDto(authentication);
        System.out.println(dto.getAccessToken());
        return dto;
    }
}
