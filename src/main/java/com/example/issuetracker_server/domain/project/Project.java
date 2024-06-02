package com.example.issuetracker_server.domain.project;

import com.example.issuetracker_server.domain.BaseTimeEntity;
import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.memberproject.MemberProject;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Issue> issues;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberProject> memberProjects;
}
