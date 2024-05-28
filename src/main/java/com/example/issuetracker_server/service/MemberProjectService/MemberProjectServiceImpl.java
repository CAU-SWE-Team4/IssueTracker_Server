package com.example.issuetracker_server.service.MemberProjectService;

import com.example.issuetracker_server.controller.dto.ProjectsSaveRequestDto;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.MemberProjectRepository;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.exception.MemberNotFoundException;
import com.example.issuetracker_server.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberProjectServiceImpl implements MemberProjectService{
    private final MemberProjectRepository memberProjectRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
    }

    public Member getMember(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
    }

    public MemberProject toEntity(Long projectId, ProjectsSaveRequestDto.Member member) {
        return MemberProject.builder().project(getProject(projectId)).member(getMember(member.getUser_id())).role(member.getRole()).build();

    }

    @Override
    @Transactional
    public Long save(Long projectId, ProjectsSaveRequestDto.Member member)
    {
        return memberProjectRepository.save(toEntity(projectId, member)).getId();
    }

}
