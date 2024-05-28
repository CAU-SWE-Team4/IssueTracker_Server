package com.example.issuetracker_server.dto.project;

import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectSaveRequestDto {

    private String title;
    private List<Member> members;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Member {
        private String user_id;
        private Role role;
    }

    @Builder
    public ProjectSaveRequestDto(String title, List<Member> members) {
        this.title = title;
        this.members = members;
    }

    public Project projectToEntity() {
        return Project.builder().title(title).build();
    }

}
