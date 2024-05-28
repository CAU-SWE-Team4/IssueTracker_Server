package com.example.issuetracker_server.dto.issue;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class IssueCreateRequestDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String description;
}
