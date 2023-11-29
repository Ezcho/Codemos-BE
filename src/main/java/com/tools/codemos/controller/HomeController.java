package com.tools.codemos.controller;


import com.tools.codemos.model.CodeEntity;
import com.tools.codemos.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired

    @GetMapping("/LB")
    @PreAuthorize("isAuthenticated()")
    public String createLeaderBoardForm() {
        return "leaderboardExample"; // HTML 템플릿의 이름
    }

}