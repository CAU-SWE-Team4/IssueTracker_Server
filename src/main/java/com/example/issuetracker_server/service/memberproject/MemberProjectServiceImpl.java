package com.example.issuetracker_server.service.memberproject;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.MemberProjectRepository;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.project.ProjectRequestDto;
import com.example.issuetracker_server.exception.MemberNotFoundException;
import com.example.issuetracker_server.exception.ProjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberProjectServiceImpl implements MemberProjectService {
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

    public MemberProject toEntity(Long projectId, ProjectRequestDto.Member member) {
        return MemberProject.builder().project(getProject(projectId)).member(getMember(member.getUser_id())).role(member.getRole()).build();

    }

    @Override
    @Transactional
    public Long save(Long projectId, ProjectRequestDto.Member member) {
        return memberProjectRepository.save(toEntity(projectId, member)).getId();
    }

    @Override
    @Transactional
    public Optional<Role> getRole(String memberId, Long projectId) {
        return memberProjectRepository.findByMemberIdAndProjectId(memberId, projectId)
                .map(MemberProject::getRole);
    }

    @Override
    @Transactional
    public List<Project> getProjectIdByMemberId(String member_id){
        List<MemberProject> memberProjects = memberProjectRepository.findByMemberId(member_id);
        List<Long> projectIds = memberProjects.stream().map(memberProject -> memberProject.getProject().getId()).collect(Collectors.toList());
        return projectRepository.findByUserId(projectIds);
    }

    public List<MemberProject> getMemberProjectByProjectId(Long project_id) {

        return memberProjectRepository.findByProjectId(project_id);
    }

    public void deleteAll(List<MemberProject> memberProjects) {
        memberProjectRepository.deleteAll(memberProjects);
    }


}
