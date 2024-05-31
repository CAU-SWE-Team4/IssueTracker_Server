package com.example.issuetracker_server.dto.project;

import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProjectRequestDto {

    @NotEmpty
    private String title;

    private List<Member> members;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Member {
        private String user_id;
        private Role role;
    }


    public Project projectToEntity() {
        return Project.builder().title(title).build();
    }

}
