package com.tools.codemos.model;

import com.tools.codemos.login.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String loginId;
    private String password;
    private String nickname;
    private UserRole role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<LeaderBoardEntity> leaderBoardEntries = new ArrayList<>();

    public Long getId(){return id;}
    public String getLoginId(){return loginId;}
    public String getPassword(){return password;}
    public String getNickname(){return nickname;}
    public UserRole getUserRole(){return role;}



}