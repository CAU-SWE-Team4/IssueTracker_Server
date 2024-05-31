package com.example.issuetracker_server.dto.project;


import com.example.issuetracker_server.domain.memberproject.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleDto {
    private String user_id;
    private Role role;

    public UserRoleDto(String user_id, Role role) {
        this.user_id = user_id;
        this.role = role;
    }
}
