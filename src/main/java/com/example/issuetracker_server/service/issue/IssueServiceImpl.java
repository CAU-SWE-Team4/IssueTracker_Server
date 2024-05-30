package com.example.issuetracker_server.service.issue;

import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.issue.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl {

    private final IssueRepository issueRepository;
    public Optional<Issue> getIssue(Long issueId) {
        return issueRepository.findById(issueId);
    }
}
