package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.dto.issue.IssueCreateRequestDto;
import com.example.issuetracker_server.dto.issue.IssueResponseDto;
import com.example.issuetracker_server.service.issue.IssueService;
import com.example.issuetracker_server.service.member.MemberService;
import com.example.issuetracker_server.service.memberproject.MemberProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<IssueResponseDto>> getIssues(@PathVariable Long projectId, @RequestParam String id, @RequestParam String pw,
                                                            @RequestParam(required = false) String sort, @RequestParam(required = false) String order) {

        if (!memberService.login(id, pw))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Optional<Role> role = memberProjectService.getRole(id, projectId);
        if (role.isEmpty())
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        try {
            List<IssueResponseDto> issues = issueService.getIssues(projectId, sort, order);
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

        Optional<IssueResponseDto> issueDetail = issueService.getIssue(projectId, issueId);
        if (issueDetail.isPresent())
            return ResponseEntity.ok(issueDetail.get());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
//
//    @GetMapping("/{issueId}/recommend")
//    public ResponseEntity<RecommendationResponse> recommendAssignee(@PathVariable Long projectId, @PathVariable Long issueId,
//                                                                    @RequestParam String id, @RequestParam String pw) {
//        RecommendationResponse recommendation = issueService.recommendAssignee(projectId, issueId, id, pw);
//        return ResponseEntity.ok(recommendation);
//    }
//
//    @PutMapping("/{issueId}/assign")
//    public ResponseEntity<Void> assignIssue(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw,
//                                            @RequestBody AssignmentRequest request) {
//        issueService.assignIssue(projectId, issueId, id, pw, request);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/{issueId}/content")
//    public ResponseEntity<Void> updateIssueContent(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw,
//                                                   @RequestBody IssueRequest request) {
//        issueService.updateIssueContent(projectId, issueId, id, pw, request);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/{issueId}/state")
//    public ResponseEntity<Void> updateIssueState(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw,
//                                                 @RequestBody StateRequest request) {
//        issueService.updateIssueState(projectId, issueId, id, pw, request);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/{issueId}")
//    public ResponseEntity<Void> deleteIssue(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw) {
//        issueService.deleteIssue(projectId, issueId, id, pw);
//        return ResponseEntity.ok().build();
//    }
}
