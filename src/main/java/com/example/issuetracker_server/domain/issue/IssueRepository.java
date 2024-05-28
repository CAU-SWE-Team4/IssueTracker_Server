package com.example.issuetracker_server.domain.issue;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByProjectIdOrderByTitleAsc(Long projectId);

    List<Issue> findByProjectIdOrderByTitleDesc(Long projectId);

    List<Issue> findByProjectIdOrderByCreatedDateAsc(Long projectId);

    List<Issue> findByProjectIdOrderByCreatedDateDesc(Long projectId);

    List<Issue> findByProjectIdOrderByStateAsc(Long projectId);

    List<Issue> findByProjectIdOrderByStateDesc(Long projectId);
}
