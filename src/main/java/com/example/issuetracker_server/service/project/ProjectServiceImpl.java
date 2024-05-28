package com.example.issuetracker_server.service.project;

import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.project.ProjectSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectsRepository;

    @Override
    @Transactional
    public Long save(ProjectSaveRequestDto requestDto) {
        return projectsRepository.save(requestDto.projectToEntity()).getId();
    }
}
