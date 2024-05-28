package com.example.issuetracker_server.service.MemberProjectService;

import com.example.issuetracker_server.controller.dto.ProjectsSaveRequestDto;

public interface MemberProjectService {

    Long save(Long projectId, ProjectsSaveRequestDto.Member member);
}
