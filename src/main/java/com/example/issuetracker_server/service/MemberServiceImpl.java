package com.example.issuetracker_server.service;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.dto.member.MemberLoginRequestDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public boolean login(MemberLoginRequestDto request) {
        Optional<Member> memberOpt = memberRepository.findById(request.getUserId());

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            return member.getPassword().equals(request.getPassword());
        }
        return false;
    }

    @Override
    public void signUp(MemberSignUpRequestDto request) {
        // 회원가입 로직 구현
        // 예를 들어, 사용자 정보를 데이터베이스에 저장
    }
}
