package com.example.issuetracker_server.service;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.dto.member.MemberLoginRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
                .id("testUser")
                .password("testPass")
                .name("Test User")
                .mail("testuser@example.com")
                .build();
    }

    @Test
    public void testLoginSuccess() {
        // Given
        MemberLoginRequestDto request = new MemberLoginRequestDto();
        request.setUserId("testUser");
        request.setPassword("testPass");

        when(memberRepository.findById("testUser")).thenReturn(Optional.of(member));

        // When
        boolean result = memberService.login(request);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    public void testLoginFailureDueToIncorrectPassword() {
        // Given
        MemberLoginRequestDto request = new MemberLoginRequestDto();
        request.setUserId("testUser");
        request.setPassword("wrongPass");

        given(memberRepository.findById(anyString())).willReturn(Optional.of(member));

        // When
        boolean result = memberService.login(request);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    public void testLoginFailureDueToNonexistentUser() {
        // Given
        MemberLoginRequestDto request = new MemberLoginRequestDto();
        request.setUserId("nonexistentUser");
        request.setPassword("testPass");

        given(memberRepository.findById(anyString())).willReturn(Optional.empty());

        // When
        boolean result = memberService.login(request);

        // Then
        assertThat(result).isFalse();
    }
}
