package com.tools.codemos.controller;
import com.tools.codemos.dto.LeaderBoardRequest;
import com.tools.codemos.model.LeaderBoardEntity;
import com.tools.codemos.service.LeaderBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/leaderBoard")
public class LeaderBoardController{
    @Autowired
    private LeaderBoardService service;

    @PostMapping("/create")
    public LeaderBoardEntity create(@RequestBody LeaderBoardRequest dto) {
        return service.createLeaderBoard(dto);
    }

    @GetMapping//리더보드 조회(기본값 100, rows = n으로 파라미터 추가)
    public Iterable<LeaderBoardEntity> read(@RequestParam(defaultValue = "100") int rows) {
        return service.getTopLeaderBoard(rows);
    }
    @GetMapping("/scope")// /api/v1/leaderBoard?start=10&end=20
    public ResponseEntity<List<LeaderBoardEntity>> getLeaderBoardEntries(@RequestParam int start, @RequestParam int end) {
        List<LeaderBoardEntity> entries = service.getLeaderBoardEntries(start, end);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/read/{id}")//Id기반으로 읽어오는 리더보드
    public LeaderBoardEntity readById(@PathVariable int id) {
        return service.getLeaderBoardById(id);
    }
    @PutMapping("/update/{id}")//id기반으로 리더보드 업데이트
    public LeaderBoardEntity update(@PathVariable int id, @RequestBody LeaderBoardEntity LB) {
        return service.updateLeaderBoard(id, LB);
    }
    @DeleteMapping("/delete/{id}")//id 기반 삭제
    public void delete(@PathVariable int id) {
        service.deleteLeaderBoard(id);
    }

}
