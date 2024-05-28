package com.example.issuetracker_server.service.project;

import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.project.ProjectsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProjectsServiceImpl implements ProjectsService {
    private final ProjectRepository projectsRepository;

    @Override
    @Transactional
    public Long save(ProjectsSaveRequestDto requestDto) {
        return projectsRepository.save(requestDto.projectToEntity()).getId();
    }
}
