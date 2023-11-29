package com.tools.codemos.service;

import com.tools.codemos.dto.LeaderBoardDTO;
import com.tools.codemos.dto.LeaderBoardRequest;
import com.tools.codemos.model.CodeEntity;
import com.tools.codemos.model.LeaderBoardEntity;
import com.tools.codemos.model.User;
import com.tools.codemos.repository.CodeRepository;
import com.tools.codemos.repository.LeaderBoardRepository;
import com.tools.codemos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderBoardService {
    @Autowired
    private LeaderBoardRepository leaderBoardRepository;
    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private UserService userService;

    public Page<LeaderBoardDTO> getLeaderBoard(int pageNo) {
        int pageSize = 10;
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by("score").descending());
        Page<LeaderBoardEntity> leaderBoardPage = leaderBoardRepository.findAll(pageRequest);
        List<LeaderBoardDTO> dtos = leaderBoardPage.getContent().stream()
                .map(entity -> new LeaderBoardDTO(entity.getId(),  entity.getScore(), entity.getLoginId(), entity.getNickname()))
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageRequest, leaderBoardPage.getTotalElements());
    }


    public LeaderBoardEntity createLeaderBoard(LeaderBoardRequest dto, String userIndex) {
        //JWT 토큰에서 추출된 사용자 index로 변경
        User user = userService.getLoginUserByLoginId(userIndex);
        //리더보드 객체 생성
        LeaderBoardEntity leaderBoard = new LeaderBoardEntity();
        leaderBoard.setScore(dto.getScore());
        leaderBoard.setUser(user); //위에서 생성한 유저객체 set
        leaderBoard.setLoginId(user.getLoginId()); //유저객체 정보 set
        leaderBoard.setNickname(user.getNickname());
        //코드엔티티 생성
        CodeEntity codeEntity = new CodeEntity();
        codeEntity.setCode(dto.getCode());
        codeEntity.setLeaderBoard(leaderBoard);
        //리더보드에 코드엔티티 연결
        leaderBoard.setCodeEntity(codeEntity);
        //DB에 저장
        leaderBoardRepository.save(leaderBoard);
        codeRepository.save(codeEntity);
        return leaderBoard;
    }
    public LeaderBoardEntity getLeaderBoardById(int id) {
        return leaderBoardRepository.findById(id).orElse(null);
    }

    public LeaderBoardEntity updateLeaderBoard(int id, LeaderBoardEntity map) {
        if (leaderBoardRepository.existsById(id)) {
            map.setId(id);
            return leaderBoardRepository.save(map);
        }
        return null;
    }
    public void deleteLeaderBoard(int id) {
        leaderBoardRepository.deleteById(id);
    }
    public Iterable<LeaderBoardEntity> getTopLeaderBoard(int rows) {
        List<LeaderBoardEntity> allMaps = (List<LeaderBoardEntity>) leaderBoardRepository.findAll();
        allMaps.sort((a, b) -> (int) (b.getScore() - a.getScore()));
        return allMaps.subList(0, Math.min(rows, allMaps.size()));
    }

    public List<LeaderBoardEntity> getLeaderBoardEntries(int start, int end) {
        int offset = start - 1; // 데이터베이스 인덱스는 0부터 시작합니다.
        int limit = end - start + 1;
        return leaderBoardRepository.findEntriesByRange(offset, limit);
    }
}
