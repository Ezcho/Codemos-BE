package com.tools.codemos.controller;

import com.tools.codemos.dto.LeaderBoardDTO;
import com.tools.codemos.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaderboard")
public class PublicAccessController {
    @Autowired
    private LeaderBoardService leaderBoardService;
    @GetMapping
    public Page<LeaderBoardDTO> getLeaderBoard(@RequestParam(defaultValue = "1") int pageno) {
        System.out.println("PageController on");
        return leaderBoardService.getLeaderBoard(pageno);
    }
    @GetMapping("/{id}")
    public ResponseEntity<String> getCodeByLeaderBoardId(@PathVariable int id) {
        String code = leaderBoardService.getCodeByLeaderBoardId(id);
        if (code != null) {
            return ResponseEntity.ok(code);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
