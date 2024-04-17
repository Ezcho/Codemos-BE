package com.tools.codemos.service;

import com.tools.codemos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import com.tools.codemos.repository.UserRepository;
import com.tools.codemos.dto.TokenDTO;
import com.tools.codemos.jwt.TokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private GoogleUserService googleUserService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        try {
            return processOAuth2User(oAuth2User);
        } catch (Exception ex) {
            throw new OAuth2AuthenticationException("throw1");
        }
    }

    private OAuth2User processOAuth2User(OAuth2User oAuth2User) {
        System.out.println("zzz");
        String email = oAuth2User.getAttribute("email");
        if (email == null) {
            throw new UsernameNotFoundException("oauth2공급자의 이메일 찾을 수 없음.");
        }

        boolean existingUser = userRepository.existsByEmail(email);
        if (!existingUser) {
            googleUserService.registerOrUpdateGoogleUser(email, oAuth2User.getAttribute("name"));
            throw new RuntimeException("존재안하니까 회원가입 로직으로 ㄱ");
        } else {
            Optional<User> user = userRepository.findByEmail(email);
            Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
            TokenDTO tokenDTO = tokenProvider.generateTokenDto(authentication);
            oAuth2User.getAttributes().put("token", tokenDTO.getAccessToken());
            System.out.println("!!!!!!"+tokenDTO.getAccessToken());
            return oAuth2User;
        }
    }
}
