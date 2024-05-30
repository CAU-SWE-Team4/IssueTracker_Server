package com.example.issuetracker_server.service.issue;


import com.example.issuetracker_server.dto.issue.IssueCreateRequestDto;
import com.example.issuetracker_server.dto.issue.IssueResponseDto;

import java.util.List;
import java.util.Optional;

public interface IssueService {

    boolean createIssue(Long projectId, String memberId, IssueCreateRequestDto request);

    List<IssueResponseDto> getIssues(Long projectId, String sort, String order);

    Optional<IssueResponseDto> getIssue(Long projectId, Long issueId);

    List<String> getRecommendAssignee(Long projectId, Long issueId);
}
