package com.example.issuetracker_server.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberLoginRequestDto {

    @NotEmpty
    private String user_id;

    @NotEmpty
    private String password;
}