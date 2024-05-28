package com.example.issuetracker_server.service.project;

import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.MemberProjectRepository;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.project.ProjectDto;
import com.example.issuetracker_server.dto.project.ProjectSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Override
    @Transactional
    public Long save(ProjectSaveRequestDto requestDto) {
        return projectRepository.save(requestDto.projectToEntity()).getId();
    }
}
