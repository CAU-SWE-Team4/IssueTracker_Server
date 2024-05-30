package com.example.issuetracker_server.dto.project;


import com.example.issuetracker_server.domain.memberproject.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserRoleDto {
    private String userId;
    private Role role;

    public UserRoleDto(String userId, Role role) {
        this.userId = userId;
        this.role = role;
    }
}
