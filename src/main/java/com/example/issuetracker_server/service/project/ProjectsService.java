package com.example.issuetracker_server.service.project;

import com.example.issuetracker_server.dto.project.ProjectsSaveRequestDto;

public interface ProjectsService {
    Long save(ProjectsSaveRequestDto requestDto);
}
