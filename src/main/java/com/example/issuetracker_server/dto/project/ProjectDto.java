package com.example.issuetracker_server.dto.project;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProjectDto {
    private Long project_id;

    @NotEmpty
    private String title;

    private String created_at;

    public ProjectDto(Long project_id, String title, String created_at) {
        this.project_id = project_id;
        this.title = title;
        this.created_at = created_at;
    }

}
