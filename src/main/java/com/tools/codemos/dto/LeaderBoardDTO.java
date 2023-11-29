package com.tools.codemos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LeaderBoardDTO {
    private int id;
    private double score;
    private String loginId;
    private String nickname;

    public LeaderBoardDTO(int id, double score, String loginId, String nickname) {
        this.id = id;
        this.score = score;
        this.loginId = loginId;
        this.nickname = nickname;
    }

}
