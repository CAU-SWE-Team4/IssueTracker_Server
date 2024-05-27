package com.example.issuetracker_server.dto.member;

import lombok.Data;

@Data
public class MemberLoginRequest {
    private String userId;
    private String password;
}