package com.example.issuetracker_server.service.issue;


import com.example.issuetracker_server.dto.issue.IssueCreateRequestDto;

public interface IssueService {

    boolean createIssue(Long projectId, String memberId, IssueCreateRequestDto request);
}
