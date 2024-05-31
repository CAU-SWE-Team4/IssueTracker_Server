package com.example.issuetracker_server.service.issue;


import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.issue.Priority;
import com.example.issuetracker_server.domain.issue.State;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.dto.issue.IssueCreateRequestDto;
import com.example.issuetracker_server.dto.issue.IssueResponseDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IssueService {

    boolean createIssue(Long projectId, String memberId, IssueCreateRequestDto request);

    List<IssueResponseDto> getIssues(Long projectId, String sort, String order);

    Optional<IssueResponseDto> getIssue(Long projectId, Long issueId);

    Map<String, List<String>> getRecommendAssignee(Long projectId, Long issueId);

    boolean assignIssue(Long projectId, Long issueId, String memberId, Priority priority);

    boolean updateIssue(String memberId, Long projectId, Long issueId, String title, String description);

    boolean updateIssueState(Long projectId, Long issueId, String id, Role role, State state);

    boolean deleteIssue(Long projectId, Long issueId);

    Optional<Issue> getIssue(Long id);
}
