package com.tools.codemos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tools.codemos.login.Authority;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private Authority authority;
    private String profilePicURL;
    @Builder
    public User(Long id, String loginId, String password, String nickname, Authority authority) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.authority = authority;
    }

    public User(String loginId, String password, String nickname, Authority authority, String profilePicURL){
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.nickname = nickname;
        this.authority = authority;
        this.profilePicURL = profilePicURL;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<LeaderBoardEntity> leaderBoardEntries = new ArrayList<>();
}