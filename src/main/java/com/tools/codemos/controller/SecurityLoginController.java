package com.tools.codemos.controller;

import com.tools.codemos.dto.JoinRequest;
import com.tools.codemos.login.LoginRequest;
import com.tools.codemos.model.User;
import com.tools.codemos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class SecurityLoginController {

    private final UserService userService;

    @GetMapping(value = {"", "/"})
    public String home(Model model, Authentication auth) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "CodeMos");

        if(auth != null) {
            User loginUser = userService.getLoginUserByLoginId(auth.getName());
            if (loginUser != null) {
                model.addAttribute("nickname", loginUser.getNickname());
            }
        }
        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "CodeMos");
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "CodeMos");

        // loginId 중복 체크
        if(userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됩니다."));
        }
        // 닉네임 중복 체크
        if(userService.checkNicknameDuplicate(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다."));
        }
        // password와 passwordCheck가 같은지 체크
        if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "바밀번호가 일치하지 않습니다."));
        }

        if(bindingResult.hasErrors()) {
            return "join";
        }

        userService.join2(joinRequest);
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "CodeMos");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @GetMapping("/info")
    public String userInfo(Model model, Authentication auth) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "CodeMos");

        User loginUser = userService.getLoginUserByLoginId(auth.getName());

        if(loginUser == null) {
            return "redirect:/home/login";
        }

        model.addAttribute("user", loginUser);
        return "info";
    }

    @GetMapping("/admin")
    public String adminPage( Model model) {
        model.addAttribute("loginType", "home");
        model.addAttribute("pageName", "CodeMos");

        return "admin";
    }
}