package com.example.issuetracker_server.service.member;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.memberproject.MemberProjectRepository;
import com.example.issuetracker_server.dto.member.MemberInfoDto;
import com.example.issuetracker_server.dto.member.MemberSignUpRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberProjectRepository memberProjectRepository;

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
    public List<MemberInfoDto> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        List<MemberInfoDto> memberInfoDtos = new ArrayList<>();
        for (Member member : members) {
            MemberInfoDto memberInfoDto = new MemberInfoDto(member.getId(), member.getName(), member.getMail());
            memberInfoDtos.add(memberInfoDto);
        }
        return memberInfoDtos;
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

    @Override
    @Transactional
    public boolean isMemberOfProject(Long projectId, String id) {
        if(memberProjectRepository.findByMemberIdAndProjectId(id, projectId).isPresent())
            return true;
        return false;
    }

    @Override
    @Transactional
    public Optional<Member> getMember(String id) {
        return memberRepository.findById(id);
    }
}
