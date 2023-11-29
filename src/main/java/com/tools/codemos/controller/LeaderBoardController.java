package com.tools.codemos.controller;
import com.tools.codemos.dto.LeaderBoardDTO;
import com.tools.codemos.dto.LeaderBoardRequest;
import com.tools.codemos.jwt.TokenProvider;
import com.tools.codemos.model.LeaderBoardEntity;
import com.tools.codemos.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/leaderBoard")
public class LeaderBoardController{
    @Autowired
    private LeaderBoardService service;
    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/create")
    public LeaderBoardEntity create(@RequestBody LeaderBoardRequest dto,
                                    @RequestHeader(value = "Authorization") String token) {
        token = token.substring(7); // "Bearer " 부분 제거
        String username = tokenProvider.getUsernameFromToken(token);
        System.out.println("UNAME: "+username);
        return service.createLeaderBoard(dto, username);
    }
}
