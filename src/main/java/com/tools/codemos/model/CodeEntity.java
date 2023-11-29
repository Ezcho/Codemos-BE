package com.tools.codemos.model;

import javax.persistence.*;

@Entity
public class CodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 1000)
    private String code;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private LeaderBoardEntity leaderBoard;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LeaderBoardEntity getLeaderBoard() {
        return leaderBoard;
    }

    public void setLeaderBoard(LeaderBoardEntity leaderBoard) {
        this.leaderBoard = leaderBoard;
    }
}

