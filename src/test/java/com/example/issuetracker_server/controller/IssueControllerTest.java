package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.dto.issue.IssueCreateRequestDto;
import com.example.issuetracker_server.service.issue.IssueService;
import com.example.issuetracker_server.service.member.MemberService;
import com.example.issuetracker_server.service.memberproject.MemberProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class IssueControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IssueService issueService;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberProjectService memberProjectService;

    @InjectMocks
    private IssueController issueController;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(issueController).build();
    }

    @Test
    public void testCreateIssueUnauthorized() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;

        when(memberService.login(id, pw)).thenReturn(false);

        // When
        mockMvc.perform(post("/project/" + projectId + "/issue")
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"issue title\", \"description\": \"issue description\"}"))

                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateIssueForbidden() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.DEV));
        boolean result = memberService.login(id, pw);

        // When
        mockMvc.perform(post("/project/" + projectId + "/issue")
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"issue title\", \"description\": \"issue description\"}"))

                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCreateIssueBadRequest() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.TESTER));
        when(issueService.createIssue(eq(projectId), eq(id), any(IssueCreateRequestDto.class))).thenReturn(false);

        // When
        mockMvc.perform(post("/project/" + projectId + "/issue")
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"issue title\", \"description\": \"issue description\"}"))

                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateIssueSuccess() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.TESTER));
        when(issueService.createIssue(eq(projectId), eq(id), any(IssueCreateRequestDto.class))).thenReturn(true);

        // When
        mockMvc.perform(post("/project/" + projectId + "/issue")
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"issue title\", \"description\": \"issue description\"}"))

                // Then
                .andExpect(status().isOk());
    }
}
