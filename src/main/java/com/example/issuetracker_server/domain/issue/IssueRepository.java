package com.example.issuetracker_server.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectId(Long projectId);

    List<Issue> findByProjectIdAndTitleContainingIgnoreCase(Long projectId, String title);

    List<Issue> findByProjectIdAndReporterIdContainingIgnoreCase(Long projectId, String reporter);

    List<Issue> findByProjectIdAndAssigneeIdContainingIgnoreCase(Long projectId, String assignee);

    List<Issue> findByProjectIdAndState(Long projectId, State state);

}
