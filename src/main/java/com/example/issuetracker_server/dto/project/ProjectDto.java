package com.example.issuetracker_server.dto.project;

import com.example.issuetracker_server.domain.project.Project;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

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
