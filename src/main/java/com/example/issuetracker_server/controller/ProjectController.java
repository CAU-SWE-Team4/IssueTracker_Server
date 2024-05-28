package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.controller.dto.ProjectsSaveRequestDto;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.exception.MemberNotFoundException;
import com.example.issuetracker_server.service.MemberProjectService.MemberProjectServiceImpl;
import com.example.issuetracker_server.service.ProjectsService.ProjectsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectsServiceImpl projectsService;
    private final MemberProjectServiceImpl memberprojectService;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    public Member getMember(String id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
    }

    @PostMapping("/")
    public ResponseEntity<Void> save(@RequestBody ProjectsSaveRequestDto requestDto, @RequestParam String id, @RequestParam String pw) {

        // Member(id).password == pw && Member(id).Role == admin
        if(Objects.equals(id, "admin") && Objects.equals(getMember(id).getPassword(), pw))
        {
            Long projectId = projectsService.save(requestDto);
            for(ProjectsSaveRequestDto.Member member: requestDto.getMembers()) {
                memberprojectService.save(projectId, member);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
