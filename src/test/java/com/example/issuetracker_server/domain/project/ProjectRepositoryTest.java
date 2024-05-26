package com.example.issuetracker_server.domain.project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testSaveAndFindProject() {
        // Given
        Project project = Project.builder()
                .title("New Project")
                .build();

        // When
        Project savedProject = projectRepository.save(project);
        Optional<Project> foundProject = projectRepository.findById(savedProject.getId());

        // Then
        assertThat(foundProject).isPresent();
        assertThat(foundProject.get().getTitle()).isEqualTo("New Project");
    }

    @Test
    public void testUpdateProject() {
        // Given
        Project project = Project.builder()
                .title("Initial Title")
                .build();

        Project savedProject = projectRepository.save(project);

        // When
        savedProject.setTitle("Updated Title");
        Project updatedProject = projectRepository.save(savedProject);
        Optional<Project> foundProject = projectRepository.findById(updatedProject.getId());

        // Then
        assertThat(foundProject).isPresent();
        assertThat(foundProject.get().getTitle()).isEqualTo("Updated Title");
    }

    @Test
    public void testDeleteProject() {
        // Given
        Project project = Project.builder()
                .title("Project to be deleted")
                .build();

        Project savedProject = projectRepository.save(project);

        // When
        projectRepository.deleteById(savedProject.getId());
        Optional<Project> foundProject = projectRepository.findById(savedProject.getId());

        // Then
        assertThat(foundProject).isNotPresent();
    }
}
