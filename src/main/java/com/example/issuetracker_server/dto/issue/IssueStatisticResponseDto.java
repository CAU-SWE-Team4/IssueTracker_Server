package com.example.issuetracker_server.dto.issue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IssueStatisticResponseDto {
    private int day_issues;
    private int month_issues;
    private int total_issues;
    private int closed_issues;
}
