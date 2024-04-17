package com.tools.codemos.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class RankingEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "code_id")
    private CodeEntity code;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int score;
    private String time;
    private int leaderBoardId;
    private String nickname;
    private String email;


    public RankingEntity() {
    }
}
