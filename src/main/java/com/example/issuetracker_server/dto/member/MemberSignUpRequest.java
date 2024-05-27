package com.example.issuetracker_server.dto.member;

import lombok.Data;

@Data
public class MemberSignUpRequest {
    private String userId;
    private String password;
    private String name;
    private String mail;
}
