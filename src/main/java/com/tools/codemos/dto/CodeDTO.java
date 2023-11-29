package com.tools.codemos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeDTO {
    private int id;
    private int score;
    private String code;

    public CodeDTO(int id, int score, String code) {
    }
}
