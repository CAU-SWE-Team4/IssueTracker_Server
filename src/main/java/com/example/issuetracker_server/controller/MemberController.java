package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.dto.member.MemberInfoDto;
import com.example.issuetracker_server.dto.member.MemberLoginRequestDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;
import com.example.issuetracker_server.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody MemberLoginRequestDto request) {
        boolean isAuthenticated = memberService.login(request.getUser_id(), request.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity<Void> signUp(@RequestBody MemberSignUpRequestDto request) {
        boolean isSignedUp = memberService.signUp(request);
        if (isSignedUp) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<MemberInfoDto>> getUsers(@RequestParam String id, @RequestParam String pw) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(
            @PathVariable String userId,
            @RequestParam String id,
            @RequestParam String pw) {

        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<MemberInfoDto> foundedMember = memberService.getUserInfo(userId);
        if (foundedMember.isPresent()) {
            return ResponseEntity.ok(foundedMember.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
