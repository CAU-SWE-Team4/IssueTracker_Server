package com.example.issuetracker_server.service.project;

import com.example.issuetracker_server.domain.memberproject.MemberProjectRepository;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.project.ProjectRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Override
    @Transactional
    public Long saveDto(ProjectRequestDto requestDto) {
        return projectRepository.save(requestDto.projectToEntity()).getId();
    }

    @Override
    @Transactional
    public Long update(Long id, ProjectRequestDto requestDto) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다 id = " + id));

        project.setTitle(requestDto.getTitle());

        return projectRepository.save(project).getId();
    }
    @Override
    @Transactional
    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }
}
