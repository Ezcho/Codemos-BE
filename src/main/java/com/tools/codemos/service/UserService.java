package com.tools.codemos.service;

import com.tools.codemos.Config.SecurityUtil;
import com.tools.codemos.dto.MypageResponseDTO;
import com.tools.codemos.dto.UserResponseDTO;
import com.tools.codemos.model.User;
import com.tools.codemos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public User getLoginUserByLoginId(String userIndex) {
        Long id = Long.parseLong(userIndex); // String을 Long으로 변환
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 ID를 가진 사용자가 없습니다: " + userIndex));
    }
    public MypageResponseDTO getUserMypage(String userId) {
        Long id = Long.valueOf(userId);

        Optional<User> userOptional = userRepository.findById(id); // 유저 정보 조회
        if (userOptional.isEmpty()) {//optional 처리
            throw new EntityNotFoundException("해당 id값에 맞는 유저가 없습니다." + id);
        }

        User user = userOptional.get();
        List<MypageResponseDTO.LeaderBoardInfo> leaderBoardInfos = user.getLeaderBoardEntries()
                .stream()
                .map(leaderBoardEntity -> new MypageResponseDTO.LeaderBoardInfo(
                        leaderBoardEntity.getId(),
                        leaderBoardEntity.getCodeEntity() != null ? leaderBoardEntity.getCodeEntity().getCode() : null))
                .collect(Collectors.toList());
        return new MypageResponseDTO(
                user.getLoginId(),
                user.getNickname(),
                user.getAuthority().toString(),
                leaderBoardInfos
        );
    }

}