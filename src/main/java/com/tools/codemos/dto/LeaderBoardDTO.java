package com.tools.codemos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaderBoardDTO {
    private int id;
    private int score;
    private String time;
    private String loginId;
    private String nickname;

    public LeaderBoardDTO(int id, int score, String loginId, String nickname,String time) {
        this.id = id;
        this.score = score;
        this.loginId = loginId;
        this.nickname = nickname;
        this.time = time;
    }
}
