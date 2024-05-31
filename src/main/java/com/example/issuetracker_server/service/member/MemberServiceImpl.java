package com.example.issuetracker_server.service.member;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.dto.member.MemberInfoDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public boolean login(String id, String pw) {
        Optional<Member> memberOpt = memberRepository.findById(id);

        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            return member.getPassword().equals(pw);
        }
        return false;
    }

    @Override
    @Transactional
    public boolean signUp(MemberSignUpRequestDto request) {
        try {
            if (memberRepository.findById(request.getUser_id()).isPresent())
                return false;
            Member member = Member.builder()
                    .id(request.getUser_id())
                    .password(request.getPassword())
                    .name(request.getName())
                    .mail(request.getEmail())
                    .build();

            memberRepository.save(member);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Optional<MemberInfoDto> getUserInfo(String userId) {
        Optional<Member> memberOptional = memberRepository.findById(userId);
        if (memberOptional.isPresent()) {
            MemberInfoDto response = new MemberInfoDto();
            response.setName(memberOptional.get().getName());
            response.setEmail(memberOptional.get().getMail());
            response.setUser_id(memberOptional.get().getId());
            return Optional.of(response);
        } else {
            return Optional.empty();
        }
    }
}
