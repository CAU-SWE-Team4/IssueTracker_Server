package com.example.issuetracker_server.service.member;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.dto.member.MemberInfoDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    boolean login(String id, String pw);

    boolean signUp(MemberSignUpRequestDto request);

    List<MemberInfoDto> getAllMembers();

    Optional<MemberInfoDto> getUserInfo(String userId);

    boolean isMemberOfProject(Long projectId, String id);

    Optional<Member> getMember(String id);
}
