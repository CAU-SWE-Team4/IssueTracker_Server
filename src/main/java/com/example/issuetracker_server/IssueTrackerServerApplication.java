package com.example.issuetracker_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class IssueTrackerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(IssueTrackerServerApplication.class, args);
    }

}

/*
target
src
└── main
    └── java
        └── com
            └── example
                └── com.example.issuetracker_server
                    ├── IssueTrackerServerApplication.java
                    ├── domain
                    │   ├── member
                    │   │   ├── Member.java
                    │   │   └── MemberRepository.java
                    │   └── project
                    │       ├── Project.java
                    │       └── ProjectRepository.java
                    ├── service
                    │   ├── MemberService.java
                    │   └── ProjectService.java
                    ├── controller
                    │   ├── MemberController.java
                    │   └── ProjectController.java
                    └── dto
                        ├── member
                        │   ├── MemberRequestDto.java
                        │   └── MemberResponseDto.java
                        └── project
                            ├── ProjectRequestDto.java
                            └── ProjectResponseDto.java
 */