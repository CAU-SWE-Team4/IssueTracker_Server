package com.example.issuetracker_server.domain.project;

import com.example.issuetracker_server.domain.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>{
    @Query("SELECT p FROM Project p")
    List<Project> findAll();
}
