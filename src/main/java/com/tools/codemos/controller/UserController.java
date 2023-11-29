package com.tools.codemos.controller;

import com.tools.codemos.dto.ChangePasswordRequestDTO;
import com.tools.codemos.dto.UserRequestDTO;
import com.tools.codemos.dto.UserResponseDTO;
import com.tools.codemos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class UserController {

    private final UserService memberService;

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

}