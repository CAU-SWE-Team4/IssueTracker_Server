package com.example.issuetracker_server.domain.member;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testSaveAndFindMember() {
        // Given
        Member member = Member.builder()
                .id("testUser")
                .password("password")
                .name("John Doe")
                .mail("john.doe@example.com")
                .build();

        // When
        Member savedMember = memberRepository.save(member);
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

        // Then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("John Doe");
    }

    @Test
    public void testUpdateMember() {
        // Given
        Member member = Member.builder()
                .id("testUser")
                .password("password")
                .name("Initial Name")
                .mail("initial@example.com")
                .build();

        Member savedMember = memberRepository.save(member);

        // When
        savedMember.setName("Updated Name");
        Member updatedMember = memberRepository.save(savedMember);
        Optional<Member> foundMember = memberRepository.findById(updatedMember.getId());

        // Then
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo("Updated Name");
    }

    @Test
    public void testDeleteMember() {
        // Given
        Member member = Member.builder()
                .id("testUser")
                .password("password")
                .name("John Doe")
                .mail("john.doe@example.com")
                .build();

        Member savedMember = memberRepository.save(member);

        // When
        memberRepository.deleteById(savedMember.getId());
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());

        // Then
        assertThat(foundMember).isNotPresent();
    }
}
