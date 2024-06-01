package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.dto.issue.*;
import com.example.issuetracker_server.service.issue.IssueService;
import com.example.issuetracker_server.service.member.MemberService;
import com.example.issuetracker_server.service.memberproject.MemberProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project/{projectId}/issue")
public class IssueController {

    private final IssueService issueService;

    private final MemberService memberService;

    private final MemberProjectService memberProjectService;

    @PostMapping
    public ResponseEntity<Void> createIssue(@PathVariable Long projectId, @RequestParam String id, @RequestParam String pw,
                                            @RequestBody IssueCreateRequestDto request) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty() || role.get() != Role.TESTER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        boolean success = issueService.createIssue(projectId, id, request);
        if (success)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @GetMapping
    public ResponseEntity<List<IssueResponseDto>> getIssues(@PathVariable Long projectId,
                                                            @RequestParam String id,
                                                            @RequestParam String pw,
                                                            @RequestParam(required = false) String filterBy,
                                                            @RequestParam(required = false) String filterValue) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        try {
            List<IssueResponseDto> issues = issueService.getIssues(projectId, filterBy, filterValue);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<IssueResponseDto> getIssue(@PathVariable Long projectId, @PathVariable Long issueId,
                                                     @RequestParam String id, @RequestParam String pw) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        Optional<IssueResponseDto> issue = issueService.getIssue(projectId, issueId);

        if (issue.isPresent())
            return ResponseEntity.ok(issue.get());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/statistic")
    public ResponseEntity<IssueStatisticResponseDto> getStatistic(@PathVariable Long projectId, @RequestParam String id, @RequestParam String pw) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return ResponseEntity.ok(issueService.getStatistic(projectId));
    }


    @GetMapping("/{issueId}/recommend")
    public ResponseEntity<Map<String, List<String>>> getRecommendAssignee(@PathVariable Long projectId, @PathVariable Long issueId,
                                                                          @RequestParam String id, @RequestParam String pw) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty() || role.get() != Role.PL)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        Map<String, List<String>> response = issueService.getRecommendAssignee(projectId, issueId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{issueId}/assign")
    public ResponseEntity<Void> assignIssue(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw,
                                            @RequestBody IssueAssignRequestDto request) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty() || role.get() != Role.PL)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        boolean result = issueService.assignIssue(projectId, issueId, request.getUser_id(), request.getPriority());
        if (result)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/{issueId}/content")
    public ResponseEntity<Void> updateIssueContent(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw,
                                                   @RequestBody IssueCreateRequestDto request) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty() || role.get() != Role.TESTER)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        boolean result = issueService.updateIssue(id, projectId, issueId, request.getTitle(), request.getDescription());
        if (result)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @PutMapping("/{issueId}/state")
    public ResponseEntity<Void> updateIssueState(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw,
                                                 @RequestBody IssueStateRequest request) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        boolean result = issueService.updateIssueState(projectId, issueId, id, role.get(), request.getState());
        if (result)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @DeleteMapping("/{issueId}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw) {
        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty() || role.get() != Role.PL)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        boolean result = issueService.deleteIssue(projectId, issueId);
        if (result)
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}
