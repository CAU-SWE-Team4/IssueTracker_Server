package com.example.issuetracker_server.service.member;

import com.example.issuetracker_server.dto.member.MemberInfoDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;

import java.util.Optional;

public interface MemberService {

    boolean login(String id, String pw);

    boolean signUp(MemberSignUpRequestDto request);

    Optional<MemberInfoDto> getUserInfo(String userId);
}
