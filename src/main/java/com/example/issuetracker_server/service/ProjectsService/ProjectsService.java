package com.example.issuetracker_server.service.ProjectsService;

import com.example.issuetracker_server.controller.dto.ProjectsSaveRequestDto;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProjectsService {
    private final ProjectRepository projectsRepository;

    @Transactional
    public Long save(ProjectsSaveRequestDto requestDto) {
        return projectsRepository.save(requestDto.projectToEntity()).getId();
    }
}
