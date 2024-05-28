package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.project.ProjectResponseDto;
import com.example.issuetracker_server.dto.project.ProjectSaveRequestDto;
import com.example.issuetracker_server.exception.MemberNotFoundException;
import com.example.issuetracker_server.service.memberproject.MemberProjectServiceImpl;
import com.example.issuetracker_server.service.project.ProjectServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectServiceImpl projectService;
    private final MemberProjectServiceImpl memberprojectService;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public Member getMember(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
    }

    @PostMapping("/")
    public ResponseEntity<Void> save(@RequestBody ProjectSaveRequestDto requestDto, @RequestParam String id, @RequestParam String pw) {

        // Member(id).password == pw && Member(id).Role == admin
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
    public ProjectResponseDto findByUser(@PathVariable String id, @PathVariable String password)
    {
        return projectService.findByUer(id);
    }
}
