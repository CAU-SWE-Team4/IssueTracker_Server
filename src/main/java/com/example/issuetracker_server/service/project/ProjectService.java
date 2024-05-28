package com.example.issuetracker_server.service.project;

import com.example.issuetracker_server.dto.project.ProjectSaveRequestDto;

public interface ProjectService {
    Long save(ProjectSaveRequestDto requestDto);
}
