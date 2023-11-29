package com.tools.codemos.controller;

import com.tools.codemos.dto.LeaderBoardDTO;
import com.tools.codemos.model.LeaderBoardEntity;
import com.tools.codemos.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/leaderboard")
public class TestController {
    @Autowired
    private LeaderBoardService leaderBoardService;
    @GetMapping
    public Page<LeaderBoardDTO> getLeaderBoard(@RequestParam(defaultValue = "1") int pageno) {
        return leaderBoardService.getLeaderBoard(pageno);
    }
}
