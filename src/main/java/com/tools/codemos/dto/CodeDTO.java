package com.tools.codemos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeDTO {
    private int id;
    private double score;
    private String code;

    public CodeDTO(int id, double score, String code) {
    }
}
