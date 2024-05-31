package com.example.issuetracker_server.dto.issue;

import com.example.issuetracker_server.domain.issue.Priority;
import com.example.issuetracker_server.domain.issue.State;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponseDto {

    private Long id;
    private Long project_id;
    private String title;
    private String description;
    private String reporter_id;
    private String assignee_id;
    private String fixer_id;
    private Priority priority;
    private State state;
    private String created_date;
    private String modified_date;
}