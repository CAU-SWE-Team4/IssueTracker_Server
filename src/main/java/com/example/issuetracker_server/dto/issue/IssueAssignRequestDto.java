package com.example.issuetracker_server.dto.issue;

import com.example.issuetracker_server.domain.issue.Priority;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class IssueAssignRequestDto {
    @NotEmpty
    private String user_id;

    @NotEmpty
    private Priority priority;
}
