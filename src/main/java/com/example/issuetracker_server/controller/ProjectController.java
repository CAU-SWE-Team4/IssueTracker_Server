package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.dto.project.ProjectDto;
import com.example.issuetracker_server.dto.project.ProjectRequestDto;
import com.example.issuetracker_server.dto.project.UserRoleDto;
import com.example.issuetracker_server.service.member.MemberService;
import com.example.issuetracker_server.service.memberproject.MemberProjectService;
import com.example.issuetracker_server.service.project.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project")
public class ProjectController {
    private final ProjectService projectService;
    private final MemberProjectService memberprojectService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody ProjectRequestDto requestDto, @RequestParam String id, @RequestParam String pw) throws JsonProcessingException {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        if (Objects.equals(id, "admin")) {

            Long projectId = projectService.saveDto(requestDto);
            for (ProjectRequestDto.Member member : requestDto.getMembers()) {
                memberprojectService.save(projectId, member);
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping
    public ResponseEntity<?> findByUser(@RequestParam String id, @RequestParam String pw) {
        if (!memberService.login(id, pw)) {
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

    @PutMapping("/{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequestDto requestDto, @RequestParam String id, @RequestParam String pw) {
        if (!memberService.login(id, pw) && !Objects.equals(id, "admin")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Project> optionalProject = projectService.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            if (requestDto.getMembers() != null) {
                projectService.update(projectId, requestDto);
                List<MemberProject> memberProjects = memberprojectService.getMemberProjectByProjectId(project.getId());
                memberprojectService.deleteAll(memberProjects);
                for (ProjectRequestDto.Member member : requestDto.getMembers()) {
                    memberprojectService.save(projectId, member);
                }
                return ResponseEntity.status(HttpStatus.OK).build();

            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId, @RequestParam String id, @RequestParam String pw) {
        Optional<Role> role = memberprojectService.getRole(id, projectId);
        if (!memberService.login(id, pw) || (!Objects.equals(id, "admin") && role.get() != Role.PL)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Project> optionalProject = projectService.findById(projectId);
        if (optionalProject.isPresent()) {
            //관련된 MemberProject 삭제
            List<MemberProject> memberProjects = memberprojectService.getMemberProjectByProjectId(projectId);
            memberprojectService.deleteAll(memberProjects);

            // project 삭제
            projectService.delete(projectId);

            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{projectId}/userRole")
    public ResponseEntity<?> getProjectUserRoles(@PathVariable Long projectId, @RequestParam String id, @RequestParam String pw) {
        if (!memberService.login(id, pw)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Project> optionalProject = projectService.findById(projectId);
        if (optionalProject.isPresent()) {
            List<MemberProject> memberProjects = memberprojectService.getMemberProjectByProjectId(projectId);
            List<UserRoleDto> userRoles = memberProjects.stream()
                    .map(mp -> new UserRoleDto(mp.getMember().getId(), mp.getRole()))
                    .collect(Collectors.toList());
//            Map<String, List<UserRoleDto>> response = new HashMap<>();
//            response.put("members", userRoles);
            return ResponseEntity.status(HttpStatus.OK).body(userRoles);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }
}
