package com.example.issuetracker_server.service.ProjectsService;

import com.example.issuetracker_server.controller.dto.ProjectsSaveRequestDto;

public interface ProjectsService {
    Long save(ProjectsSaveRequestDto requestDto);
}
