package com.tools.codemos.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestDTO {
    private String email;
    private String exPassword;
    private String newPassword;
}