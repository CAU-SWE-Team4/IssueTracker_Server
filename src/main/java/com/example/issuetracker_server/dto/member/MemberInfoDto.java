package com.example.issuetracker_server.dto.member;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberInfoDto {

    @NotEmpty
    private String user_id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;

}
