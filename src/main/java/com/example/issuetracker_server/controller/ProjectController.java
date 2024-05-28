package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.service.member.MemberService;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.project.ProjectDto;
import com.example.issuetracker_server.dto.project.ProjectSaveRequestDto;
import com.example.issuetracker_server.exception.MemberNotFoundException;
import com.example.issuetracker_server.service.memberproject.MemberProjectServiceImpl;
import com.example.issuetracker_server.service.project.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectServiceImpl projectService;
    private final MemberProjectServiceImpl memberprojectService;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public Member getMember(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
    }

    @PostMapping("/")
    public ResponseEntity<Void> save(@RequestBody ProjectSaveRequestDto requestDto, @RequestParam String id, @RequestParam String pw) {
        if (Objects.equals(id, "admin") && Objects.equals(getMember(id).getPassword(), pw)) {
            Long projectId = projectService.save(requestDto);
            for (ProjectSaveRequestDto.Member member : requestDto.getMembers()) {
                memberprojectService.save(projectId, member);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/")
    public ResponseEntity<?> findByUser(@RequestParam String id, @RequestParam String pw)
    {
        if(!memberService.login(id, pw))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Project> projects = memberprojectService.getProjectIdByMemberId(id);
        List<ProjectDto> projectDtos = projects.stream()
                .map(project -> new ProjectDto(project.getId(), project.getTitle(), project.getCreatedDate().toString()))
                .collect(Collectors.toList());
        Map<String, List<ProjectDto>> response = new HashMap<>();
        response.put("projects", projectDtos);
        return ResponseEntity.ok(response);
    }
}
