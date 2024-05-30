package com.example.issuetracker_server.service.issue;

import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.member.Member;

import java.util.Optional;

public interface IssueService {

    Optional<Issue> getIssue(Long id);
}
