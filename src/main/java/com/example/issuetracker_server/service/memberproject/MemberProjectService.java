package com.example.issuetracker_server.service.memberproject;

import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.dto.project.ProjectSaveRequestDto;

import java.util.Optional;

public interface MemberProjectService {

    Long save(Long projectId, ProjectSaveRequestDto.Member member);

    Optional<Role> getRole(String uId, Long pId);
}
