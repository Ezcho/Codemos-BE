package com.tools.codemos.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class RankingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int score;
    private String time;
    private int leaderBoardId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "code_id")
    private CodeEntity code;


    public RankingEntity() {}

    public RankingEntity(User user, CodeEntity code, int score, String date,int leaderBoardId) {
        this.user = user;
        this.code = code;
        this.score = score;
        this.leaderBoardId = leaderBoardId;
    }
}
