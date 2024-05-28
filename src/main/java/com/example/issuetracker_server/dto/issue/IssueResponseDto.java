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
    private Long projectId;
    private String title;
    private String description;
    private String reporterId;
    private String assigneeId;
    private String fixerId;
    private Priority priority;
    private State state;
    private String createdDate;
    private String modifiedDate;
}