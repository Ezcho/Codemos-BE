package com.tools.codemos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class CodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 10000)
    private String code;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
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

