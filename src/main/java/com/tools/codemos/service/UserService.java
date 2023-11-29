package com.tools.codemos.service;

import com.tools.codemos.Config.SecurityUtil;
import com.tools.codemos.dto.UserResponseDTO;
import com.tools.codemos.model.User;
import com.tools.codemos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDTO getMyInfoBySecurity() {
        return userRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(UserResponseDTO::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    @Transactional
    public UserResponseDTO changeMemberNickname(String loginId, String nickname) {
        User user = userRepository.findByLoginId(loginId).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        user.setNickname(nickname);
        return UserResponseDTO.of(userRepository.save(user));
    }

    public User getLoginUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("해당 로그인 ID를 가진 사용자가 없습니다: " + loginId));
    }
}