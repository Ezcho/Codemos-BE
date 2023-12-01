package com.tools.codemos.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MypageResponseDTO {
    private String loginId;
    private String nickname;
    private String role;
    private List<LeaderBoardInfo> leaderBoards;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LeaderBoardInfo {
        private int leaderBoardId;
        private String code;
    }
}
