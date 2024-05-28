package com.example.issuetracker_server.service.memberproject;

import com.example.issuetracker_server.dto.project.ProjectsSaveRequestDto;

public interface MemberProjectService {

    Long save(Long projectId, ProjectsSaveRequestDto.Member member);
}
