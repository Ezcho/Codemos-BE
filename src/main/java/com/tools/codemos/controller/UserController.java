package com.tools.codemos.controller;

import com.tools.codemos.dto.MypageResponseDTO;
import com.tools.codemos.dto.UserRequestDTO;
import com.tools.codemos.dto.UserResponseDTO;
import com.tools.codemos.jwt.TokenProvider;
import com.tools.codemos.model.RankingEntity;
import com.tools.codemos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService memberService;
    @Autowired
    private final TokenProvider tokenProvider;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyMemberInfo() {
        UserResponseDTO myInfoBySecurity = memberService.getMyInfoBySecurity();
        System.out.println(myInfoBySecurity.getNickname());
        return ResponseEntity.ok((myInfoBySecurity));
        // return ResponseEntity.ok(memberService.getMyInfoBySecurity());
    }

    @PostMapping("/nickname")
    public ResponseEntity<UserResponseDTO> setMemberNickname(@RequestBody UserRequestDTO request) {
        return ResponseEntity.ok(memberService.changeMemberNickname(request.getLoginId(), request.getNickname()));
    }

    private final UserService userService;

    @GetMapping("/mypage")
    public ResponseEntity<MypageResponseDTO> getMyPage(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername(); //사용자 ID를 UserDetails에서 추출
        System.out.println("Uid: " + userId);
        MypageResponseDTO mypageResponseDTO = userService.getUserMypage(userId);
        return ResponseEntity.ok(mypageResponseDTO);
    }

    @PostMapping("/updateRanking/{id}")
    public ResponseEntity<RankingEntity> copyLeaderBoardToRanking(@PathVariable int id, @RequestHeader("Authorization") String accessToken) {
        if (accessToken == null || !accessToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String token = accessToken.substring(7);
        boolean isValid = tokenProvider.validateToken(token);
        if (!isValid) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        RankingEntity rankingEntity = userService.copyLeaderBoardToRanking(id);
        if (rankingEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(rankingEntity);
    }

}