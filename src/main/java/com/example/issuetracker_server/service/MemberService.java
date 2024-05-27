package com.example.issuetracker_server.service;

import com.example.issuetracker_server.dto.member.MemberLoginRequestDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;

public interface MemberService {

    boolean login(MemberLoginRequestDto request);

    void signUp(MemberSignUpRequestDto request);
}
