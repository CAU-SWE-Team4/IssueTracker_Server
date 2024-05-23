package com.example.issuetracker_server.domain.project;

import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectRepositoryTest {

    @Autowired
    ProjectRepository projectRepository;

    @After
    public void cleanup() { projectRepository.deleteAll();}

    @Test
    public void savePost() {
        //given

        String title = "project1";

        projectRepository.save(Project.builder().title(title).build());

        //when
        List<Project> projectList = projectRepository.findAll();

        //then
        Project project = projectList.get(0);
        assertThat(project.getTitle()).isEqualTo(title);
    }

    //JPA Auditing Test
    @Test
    public void BaseTimeEntity_register() {
        //given
        LocalDateTime now = LocalDateTime.of(2019,6,4,0,0,0);
        projectRepository.save(Project.builder().title("title").build());

        //when
        List<Project> projectList = projectRepository.findAll();

        //then
        Project project = projectList.get(0);

        System.out.println(">>>>>>> createDate = " + project.getCreatedDate() + ", modifiedDate = " + project.getModifiedDate());
        assertThat(project.getCreatedDate()).isAfter(now);
        assertThat(project.getModifiedDate()).isAfter(now);
    }

}
