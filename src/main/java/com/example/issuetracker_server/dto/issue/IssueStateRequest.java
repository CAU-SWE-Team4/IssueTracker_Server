package com.example.issuetracker_server.dto.issue;

import com.example.issuetracker_server.domain.issue.State;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssueStateRequest {
    private State state;
}
