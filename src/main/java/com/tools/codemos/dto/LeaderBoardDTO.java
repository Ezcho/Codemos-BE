package com.tools.codemos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaderBoardDTO {
    private int id;
    private int score;
    private String time;
    private String email;
    private String nickname;

    public LeaderBoardDTO(int id, int score, String email, String nickname,String time) {
        this.id = id;
        this.score = score;
        this.email = email;
        this.nickname = nickname;
        this.time = time;
    }
}
