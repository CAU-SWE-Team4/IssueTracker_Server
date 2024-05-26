package com.example.issuetracker_server.domain.memberproject;

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
public class MemberProjectRepositoryTest {

    @Autowired
    private MemberProjectRepository memberProjectRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void testSaveAndFindMemberProject() {
        // Given
        Member member = memberRepository.save(Member.builder()
                .id("user1")
                .password("pass")
                .name("User One")
                .mail("user1@example.com")
                .build());

        Project project = projectRepository.save(Project.builder()
                .title("Project Title")
                .build());

        MemberProject memberProject = MemberProject.builder()
                .member(member)
                .project(project)
                .role(Role.DEV)
                .build();

        // When
        MemberProject savedMemberProject = memberProjectRepository.save(memberProject);
        Optional<MemberProject> foundMemberProject = memberProjectRepository.findById(savedMemberProject.getId());

        // Then
        assertThat(foundMemberProject).isPresent();
        assertThat(foundMemberProject.get().getMember().getName()).isEqualTo("User One");
        assertThat(foundMemberProject.get().getProject().getTitle()).isEqualTo("Project Title");
    }

    @Test
    public void testUpdateMemberProject() {
        // Given
        Member member = memberRepository.save(Member.builder()
                .id("user2")
                .password("pass")
                .name("User Two")
                .mail("user2@example.com")
                .build());

        Project project = projectRepository.save(Project.builder()
                .title("Initial Project Title")
                .build());

        MemberProject memberProject = memberProjectRepository.save(MemberProject.builder()
                .member(member)
                .project(project)
                .role(Role.TESTER)
                .build());

        // When
        memberProject.setRole(Role.PL);
        MemberProject updatedMemberProject = memberProjectRepository.save(memberProject);
        Optional<MemberProject> foundMemberProject = memberProjectRepository.findById(updatedMemberProject.getId());

        // Then
        assertThat(foundMemberProject).isPresent();
        assertThat(foundMemberProject.get().getRole()).isEqualTo(Role.PL);
    }

    @Test
    public void testDeleteMemberProject() {
        // Given
        Member member = memberRepository.save(Member.builder()
                .id("user3")
                .password("pass")
                .name("User Three")
                .mail("user3@example.com")
                .build());

        Project project = projectRepository.save(Project.builder()
                .title("Another Project Title")
                .build());

        MemberProject memberProject = memberProjectRepository.save(MemberProject.builder()
                .member(member)
                .project(project)
                .role(Role.DEV)
                .build());

        // When
        memberProjectRepository.deleteById(memberProject.getId());
        Optional<MemberProject> foundMemberProject = memberProjectRepository.findById(memberProject.getId());

        // Then
        assertThat(foundMemberProject).isNotPresent();
    }
}
