package com.example.issuetracker_server.dto.issue;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IssueCreateRequestDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String description;
}
