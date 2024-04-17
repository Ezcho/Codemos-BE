package com.tools.codemos.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor // 기본 생성자 추가
public class RankingDTO {
    private int id;
    private int score;
    private String email;
    private String nickname;
    private String time;
    private int codeId;

    public RankingDTO(int id, int score, String email, String nickname, String time, int codeId) {
        this.id = id;
        this.score = score;
        this.email = email;
        this.nickname = nickname;
        this.time = time;
        this.codeId = codeId;
    }
}

