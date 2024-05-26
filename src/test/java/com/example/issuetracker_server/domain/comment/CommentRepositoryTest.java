package com.example.issuetracker_server.domain.comment;

import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.issue.IssueRepository;
import com.example.issuetracker_server.domain.issue.Priority;
import com.example.issuetracker_server.domain.issue.State;
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
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testSaveAndFindComment() {
        // Given
        Member author = memberRepository.save(Member.builder()
                .id("author")
                .password("password")
                .name("Author Name")
                .mail("author@example.com")
                .build());

        Project project = projectRepository.save(Project.builder()
                .title("Project Title")
                .build());

        Issue issue = issueRepository.save(Issue.builder()
                .title("Issue Title")
                .description("Issue Description")
                .project(project)
                .reporter(author)
                .state(State.NEW)
                .priority(Priority.HIGH)
                .build());

        Comment comment = Comment.builder()
                .author(author)
                .issue(issue)
                .content("This is a comment")
                .build();

        // When
        Comment savedComment = commentRepository.save(comment);
        Optional<Comment> foundComment = commentRepository.findById(savedComment.getId());

        // Then
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("This is a comment");
        assertThat(foundComment.get().getAuthor().getName()).isEqualTo("Author Name");
    }

    @Test
    public void testUpdateComment() {
        // Given
        Member author = memberRepository.save(Member.builder()
                .id("author")
                .password("password")
                .name("Author Name")
                .mail("author@example.com")
                .build());

        Project project = projectRepository.save(Project.builder()
                .title("Project Title")
                .build());

        Issue issue = issueRepository.save(Issue.builder()
                .title("Issue Title")
                .description("Issue Description")
                .project(project)
                .reporter(author)
                .state(State.NEW)
                .priority(Priority.HIGH)
                .build());

        Comment comment = commentRepository.save(Comment.builder()
                .author(author)
                .issue(issue)
                .content("Initial Comment")
                .build());

        // When
        comment.setContent("Updated Comment");
        Comment updatedComment = commentRepository.save(comment);
        Optional<Comment> foundComment = commentRepository.findById(updatedComment.getId());

        // Then
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getContent()).isEqualTo("Updated Comment");
    }

    @Test
    public void testDeleteComment() {
        // Given
        Member author = memberRepository.save(Member.builder()
                .id("author")
                .password("password")
                .name("Author Name")
                .mail("author@example.com")
                .build());

        Project project = projectRepository.save(Project.builder()
                .title("Project Title")
                .build());

        Issue issue = issueRepository.save(Issue.builder()
                .title("Issue Title")
                .description("Issue Description")
                .project(project)
                .reporter(author)
                .state(State.NEW)
                .priority(Priority.HIGH)
                .build());

        Comment comment = commentRepository.save(Comment.builder()
                .author(author)
                .issue(issue)
                .content("Comment to be deleted")
                .build());

        // When
        commentRepository.deleteById(comment.getId());
        Optional<Comment> foundComment = commentRepository.findById(comment.getId());

        // Then
        assertThat(foundComment).isNotPresent();
    }
}
