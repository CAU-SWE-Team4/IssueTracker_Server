package com.example.issuetracker_server.service.project;

import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.dto.project.ProjectRequestDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ProjectService {
    Long saveDto(ProjectRequestDto requestDto);

    Long update(Long id, ProjectRequestDto requestDto);
    Optional<Project> findById(Long id);

    void delete(Long id);
}
