package com.example.issuetracker_server.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectId(Long projectId);

    List<Issue> findByProjectIdAndTitleContainingIgnoreCase(Long projectId, String title);

    List<Issue> findByProjectIdAndReporterContainingIgnoreCase(Long projectId, String reporter);

    List<Issue> findByProjectIdAndAssigneeContainingIgnoreCase(Long projectId, String assignee);

    List<Issue> findByProjectIdAndStateContainingIgnoreCase(Long projectId, String state);
}
