package com.tools.codemos.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class LeaderBoardEntity {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;
     private int score;
     private String time;
     @ManyToOne
     @JoinColumn(name = "user_id")
    private User user;

    private String loginId; // User 객체의 loginId
    private String nickname; // User 객체의 nickname
    @OneToOne(mappedBy = "leaderBoard", cascade = CascadeType.ALL)
    private CodeEntity codeEntity;
    public CodeEntity getCodeEntity() {
        return codeEntity;
    }
    public void setCodeEntity(CodeEntity codeEntity) {
        this.codeEntity = codeEntity;
    }
}
