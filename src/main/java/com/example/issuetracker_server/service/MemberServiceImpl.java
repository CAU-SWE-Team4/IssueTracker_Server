package com.example.issuetracker_server.service;

import com.example.issuetracker_server.dto.member.MemberLoginRequest;
import com.example.issuetracker_server.dto.member.MemberSignUpRequest;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Override
    public boolean login(MemberLoginRequest request) {
        // 로그인 로직 구현
        // 예를 들어, 데이터베이스에서 사용자 정보를 확인하고 비밀번호 검증
        return true; // 로그인 성공 시 true 반환
    }

    @Override
    public void signUp(MemberSignUpRequest request) {
        // 회원가입 로직 구현
        // 예를 들어, 사용자 정보를 데이터베이스에 저장
    }
}
