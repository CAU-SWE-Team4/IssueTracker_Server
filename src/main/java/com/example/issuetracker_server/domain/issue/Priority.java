package com.example.issuetracker_server.domain.issue;

public enum Priority {
    BLOCKER,  // 매우 긴급한 우선순위
    CRITICAL,      // 높은 우선순위
    MAJOR,    // 중간 우선순위
    MINOR,       // 낮은 우선순위
    TRIVIAL
}

// 우선순위: blocker/critical/major/minor/trivial (기본값은 major)