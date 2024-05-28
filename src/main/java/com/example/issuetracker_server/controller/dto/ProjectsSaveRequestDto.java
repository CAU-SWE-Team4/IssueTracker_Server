package com.example.issuetracker_server.controller.dto;

import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Getter
@NoArgsConstructor
public class ProjectsSaveRequestDto {

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
    public ProjectsSaveRequestDto(String title, List<Member> members) {
        this.title = title;
        this.members = members;
    }
    public Project projectToEntity() {
        return Project.builder().title(title).build();
    }

}
