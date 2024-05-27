package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.dto.member.MemberLoginRequestDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;
import com.example.issuetracker_server.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberLoginRequestDto request) {
        boolean isAuthenticated = memberService.login(request);
        if (isAuthenticated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody MemberSignUpRequestDto request) {
        try {
            memberService.signUp(request);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
