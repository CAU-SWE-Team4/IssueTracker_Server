package com.example.issuetracker_server.domain.project;

import com.example.issuetracker_server.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Project extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable=false)
    private String title;

    @Builder
    public Project(String title) {
        this.title = title;
    }

    // update, read
    public void update(String title) {
        this.title = title;
    }

}
