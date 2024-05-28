package com.example.issuetracker_server.service.memberproject;

import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.dto.project.ProjectsSaveRequestDto;

import java.util.Optional;

public interface MemberProjectService {

    Long save(Long projectId, ProjectsSaveRequestDto.Member member);

    Optional<Role> getRole(String uId, Long pId);
}
