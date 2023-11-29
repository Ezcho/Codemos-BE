package com.tools.codemos.dto;

public class LeaderBoardRequest {
    private int score;
    private String code;
    private String time;
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getTime(){return time;}
    public void setTime(){this.time = time;}
}
