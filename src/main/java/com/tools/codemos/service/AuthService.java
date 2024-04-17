package com.tools.codemos.service;

import com.tools.codemos.dto.TokenDTO;
import com.tools.codemos.dto.UserRequestDTO;
import com.tools.codemos.dto.UserResponseDTO;
import com.tools.codemos.jwt.TokenProvider;
import com.tools.codemos.model.User;
import com.tools.codemos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public UserResponseDTO signup(UserRequestDTO requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        User user = requestDto.toUser(passwordEncoder);
        return UserResponseDTO.of(userRepository.save(user));
    }

    public TokenDTO login(UserRequestDTO requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authentication);
    }
    public TokenDTO googleLogin(String email) {
        Authentication authentication = getAuthentication(email);
        return tokenProvider.generateTokenDto(authentication);
    }
    public Authentication getAuthentication(String email) {
        return new UsernamePasswordAuthenticationToken(email, null);
    }

}