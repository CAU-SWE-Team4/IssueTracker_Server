package com.example.issuetracker_server.dto.member;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MemberSignUpRequestDto {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String mail;

    @NotEmpty
    private String password;
}
