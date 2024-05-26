package com.example.issuetracker_server.domain.issue;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class IssueRepositoryTest {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testSaveAndFindIssue() {
        // Given
        Member reporter = memberRepository.save(Member.builder()
                .id("reporter")
                .password("password")
                .name("Reporter Name")
                .mail("reporter@example.com")
                .build());

        Project project = projectRepository.save(Project.builder()
                .title("Project Title")
                .build());

        Issue issue = Issue.builder()
                .title("Issue Title")
                .description("Issue Description")
                .project(project)
                .reporter(reporter)
                .state(State.NEW)
                .priority(Priority.HIGH)
                .build();

        // When
        Issue savedIssue = issueRepository.save(issue);
        Optional<Issue> foundIssue = issueRepository.findById(savedIssue.getId());

        // Then
        assertThat(foundIssue).isPresent();
        assertThat(foundIssue.get().getTitle()).isEqualTo("Issue Title");
        assertThat(foundIssue.get().getReporter().getName()).isEqualTo("Reporter Name");
    }

    @Test
    public void testUpdateIssue() {
        // Given
        Member reporter = memberRepository.save(Member.builder()
                .id("reporter")
                .password("password")
                .name("Reporter Name")
                .mail("reporter@example.com")
                .build());

        Project project = projectRepository.save(Project.builder()
                .title("Project Title")
                .build());

        Issue issue = issueRepository.save(Issue.builder()
                .title("Initial Title")
                .description("Initial Description")
                .project(project)
                .reporter(reporter)
                .state(State.NEW)
                .priority(Priority.MEDIUM)
                .build());

        // When
        issue.setTitle("Updated Title");
        issue.setPriority(Priority.LOW);
        Issue updatedIssue = issueRepository.save(issue);
        Optional<Issue> foundIssue = issueRepository.findById(updatedIssue.getId());

        // Then
        assertThat(foundIssue).isPresent();
        assertThat(foundIssue.get().getTitle()).isEqualTo("Updated Title");
        assertThat(foundIssue.get().getPriority()).isEqualTo(Priority.LOW);
    }

    @Test
    public void testDeleteIssue() {
        // Given
        Member reporter = memberRepository.save(Member.builder()
                .id("reporter")
                .password("password")
                .name("Reporter Name")
                .mail("reporter@example.com")
                .build());

        Project project = projectRepository.save(Project.builder()
                .title("Project Title")
                .build());

        Issue issue = issueRepository.save(Issue.builder()
                .title("Issue to be deleted")
                .description("Description")
                .project(project)
                .reporter(reporter)
                .state(State.CLOSED)
                .priority(Priority.LOW)
                .build());

        // When
        issueRepository.deleteById(issue.getId());
        Optional<Issue> foundIssue = issueRepository.findById(issue.getId());

        // Then
        assertThat(foundIssue).isNotPresent();
    }
}
