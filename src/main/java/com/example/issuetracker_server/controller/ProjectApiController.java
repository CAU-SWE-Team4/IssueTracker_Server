package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.controller.dto.ProjectsSaveRequestDto;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.exception.MemberNotFoundException;
import com.example.issuetracker_server.exception.ProjectNotFoundException;
import com.example.issuetracker_server.service.MemberProjectService.MemberProjectService;
import com.example.issuetracker_server.service.ProjectsService.ProjectsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
public class ProjectApiController {
    private final ProjectsService projectsService;
    private final MemberProjectService memberprojectService;
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

    @PostMapping("/project")
    public Long save(@RequestBody ProjectsSaveRequestDto requestDto, @RequestParam String id, @RequestParam String pw) {

        Long projectId = projectsService.save(requestDto);

        // Member(id).password == pw && Member(id).Role == admin
        if(Objects.equals(id, "admin") && Objects.equals(getMember(id).getPassword(), pw))
        {
            for(ProjectsSaveRequestDto.Member member: requestDto.getMembers()) {
                memberprojectService.save(projectId, member);
            }
            return projectId;
        }
        return 0L;
    }
}
