package com.example.issuetracker_server.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberLoginRequestDto {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String password;
}