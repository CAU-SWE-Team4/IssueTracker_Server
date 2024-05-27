package com.example.issuetracker_server.service;

import com.example.issuetracker_server.dto.member.MemberLoginRequest;
import com.example.issuetracker_server.dto.member.MemberSignUpRequest;

public interface MemberService {

    boolean login(MemberLoginRequest request);

    void signUp(MemberSignUpRequest request);
}
