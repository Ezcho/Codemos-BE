package com.tools.codemos.service;



import com.tools.codemos.dto.TokenDTO;
import com.tools.codemos.jwt.TokenProvider;
import com.tools.codemos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenProvider jwtTokenProvider;  // JWT 토큰 생성기

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String email = oAuth2User.getAttribute("email");

        UserDetails userDetails = userRepository.findByLoginId(email)
                .map(user -> new User(user.getLoginId(), user.getPassword(), getAuthorities(user)))
                .orElseGet(() -> (User) registerNewUser(email, oAuth2User.getAttributes()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        TokenDTO tokenDTO = jwtTokenProvider.generateTokenDto(authentication);  // JWT 생성

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email");
    }

    private Collection<? extends GrantedAuthority> getAuthorities(com.tools.codemos.model.User user) {
        return Collections.singletonList(new SimpleGrantedAuthority(user.getAuthority().toString()));
    }

    private UserDetails registerNewUser(String email, Map<String, Object> attributes) {
        // 새로운 사용자 등록 로직 구현
        return null;
    }
}
