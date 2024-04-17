package com.tools.codemos.dto;

import com.tools.codemos.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private String email;
    private String nickname;

    public static UserResponseDTO of(User user) {
        return UserResponseDTO.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}