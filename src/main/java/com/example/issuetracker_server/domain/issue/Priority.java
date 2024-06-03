package com.example.issuetracker_server.domain.issue;

public enum Priority {
    BLOCKER,  // 매우 긴급한 우선순위
    CRITICAL,      // 높은 우선순위
    MAJOR,    // 중간 우선순위
    MINOR,       // 낮은 우선순위
    TRIVIAL;

    public static int toValue(Priority priority) {
        return switch (priority) {
            case BLOCKER -> 5;
            case CRITICAL -> 4;
            case MAJOR -> 3;
            case MINOR -> 2;
            case TRIVIAL -> 1;
            default -> throw new IllegalArgumentException("Unknown priority: " + priority);
        };
    }
}

// 우선순위: blocker/critical/major/minor/trivial (기본값은 major)