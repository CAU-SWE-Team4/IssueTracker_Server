package com.example.issuetracker_server.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberSignUpRequestDto {

    @NotEmpty
    private String user_id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
