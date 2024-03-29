package com.tools.codemos.controller;

import com.tools.codemos.dto.TokenDTO;
import com.tools.codemos.dto.UserRequestDTO;
import com.tools.codemos.dto.UserResponseDTO;
import com.tools.codemos.jwt.TokenProvider;
import com.tools.codemos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;


    @PostMapping("/sign")
    public ResponseEntity<UserResponseDTO> CreateUserInfo(@RequestBody UserRequestDTO requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }
    @DeleteMapping("/sign")
    public void deleteUserInfo(){}
    @PutMapping("/sign")
    public void updateUserInfo(){}
    @PostMapping("/changepwd")
    public void changePwd(){}
    @GetMapping("/findLoginId")
    public void FindLoginId(){}
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody UserRequestDTO requestDto) {
        return ResponseEntity.ok(authService.login(requestDto));
    }
    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestHeader(value="Authorization") String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            boolean isValid = tokenProvider.validateToken(token);
            if (isValid) {
                return ResponseEntity.ok("Token is valid.");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
    }
}