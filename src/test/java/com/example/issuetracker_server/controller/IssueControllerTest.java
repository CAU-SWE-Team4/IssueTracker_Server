package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.issue.Priority;
import com.example.issuetracker_server.domain.issue.State;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.dto.issue.IssueAssignRequestDto;
import com.example.issuetracker_server.dto.issue.IssueCreateRequestDto;
import com.example.issuetracker_server.dto.issue.IssueResponseDto;
import com.example.issuetracker_server.dto.issue.IssueStateRequest;
import com.example.issuetracker_server.service.issue.IssueService;
import com.example.issuetracker_server.service.member.MemberService;
import com.example.issuetracker_server.service.memberproject.MemberProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    public void testGetIssuesSuccess() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        String filterBy = "state";
        String filterValue = "open";

        List<IssueResponseDto> issues = new ArrayList<>();
        issues.add(IssueResponseDto.builder()
                .id(1L)
                .title("issue title")
                .description("issue description")
                .state(State.NEW).build());
        issues.add(IssueResponseDto.builder()
                .id(2L)
                .title("issue title2")
                .description("issue description2")
                .state(State.ASSIGNED).build());


        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.TESTER));
        when(issueService.getIssues(projectId, filterBy, filterValue)).thenReturn(issues);

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue")
                        .param("id", id)
                        .param("pw", pw)
                        .param("filterBy", filterBy)
                        .param("filterValue", filterValue)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("issue title"))
                .andExpect(jsonPath("$[0].description").value("issue description"))
                .andExpect(jsonPath("$[0].state").value(State.NEW.toString()))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("issue title2"))
                .andExpect(jsonPath("$[1].description").value("issue description2"))
                .andExpect(jsonPath("$[1].state").value(State.ASSIGNED.toString()));
    }

    @Test
    public void testGetIssuesUnauthorized() throws Exception {
        // Given
        String id = "testuser";
        String pw = "wrongpassword";
        Long projectId = 1L;

        when(memberService.login(id, pw)).thenReturn(false);

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue")
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetIssuesForbidden() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.empty());

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue")
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetIssuesBadRequest() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        String filterBy = "invalidFilter";
        String filterValue = "value";

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.TESTER));
        when(issueService.getIssues(projectId, filterBy, filterValue)).thenThrow(new RuntimeException());

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue")
                        .param("id", id)
                        .param("pw", pw)
                        .param("filterBy", filterBy)
                        .param("filterValue", filterValue)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetIssueSuccess() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        Long issueId = 2L;

        IssueResponseDto issue = IssueResponseDto.builder()
                .id(issueId)
                .title("issue title")
                .description("issue description")
                .state(State.NEW)
                .build();

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.TESTER));
        when(issueService.getIssue(projectId, issueId)).thenReturn(Optional.of(issue));

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue/" + issueId)
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(issueId))
                .andExpect(jsonPath("$.title").value("issue title"))
                .andExpect(jsonPath("$.description").value("issue description"))
                .andExpect(jsonPath("$.state").value(State.NEW.toString()));
    }

    @Test
    public void testGetIssueUnauthorized() throws Exception {
        // Given
        String id = "testuser";
        String pw = "wrongpassword";
        Long projectId = 1L;
        Long issueId = 2L;

        when(memberService.login(id, pw)).thenReturn(false);

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue/" + issueId)
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetIssueForbidden() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        Long issueId = 2L;

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.empty());

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue/" + issueId)
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetIssueNotFound() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        Long issueId = 2L;

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.TESTER));
        when(issueService.getIssue(projectId, issueId)).thenReturn(Optional.empty());

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue/" + issueId)
                        .param("id", id)
                        .param("pw", pw)
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetRecommendAssigneeUnauthorized() throws Exception {
        // Given
        String id = "testuser";
        String pw = "wrongpassword";
        Long projectId = 1L;
        Long issueId = 1L;

        when(memberService.login(id, pw)).thenReturn(false);

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue/" + issueId + "/recommend")
                        .param("id", id)
                        .param("pw", pw)
                        .param("projectId", String.valueOf(projectId))
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetRecommendAssigneeForbidden() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        Long issueId = 1L;

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.TESTER));

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue/" + issueId + "/recommend")
                        .param("id", id)
                        .param("pw", pw)
                        .param("projectId", String.valueOf(projectId))
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetRecommendAssigneeSuccess() throws Exception {
        // Given
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        Long issueId = 1L;
        List<String> assignees = List.of("user1", "user2", "user3");
        Map<String, List<String>> recommendedAssignees = new HashMap<>();
        recommendedAssignees.put("dev_ids", assignees);

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.PL));
        when(issueService.getRecommendAssignee(projectId, issueId)).thenReturn(recommendedAssignees);

        // When
        mockMvc.perform(get("/project/" + projectId + "/issue/" + issueId + "/recommend")
                        .param("id", id)
                        .param("pw", pw)
                        .param("projectId", String.valueOf(projectId))
                        .contentType(MediaType.APPLICATION_JSON))

                // Then
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.dev_ids[0]", is("user1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dev_ids[1]", is("user2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dev_ids[2]", is("user3")));
    }

    @Test
    public void testAssignIssueUnauthorized() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        String id = "testuser";
        String pw = "wrongpassword";
        Long projectId = 1L;
        Long issueId = 1L;
        IssueAssignRequestDto request = new IssueAssignRequestDto();
        request.setUser_id("user1");
        request.setPriority(Priority.CRITICAL);

        when(memberService.login(id, pw)).thenReturn(false);

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/assign")
                        .param("id", id)
                        .param("pw", pw)
                        .param("projectId", String.valueOf(projectId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAssignIssueForbidden() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        Long issueId = 1L;
        IssueAssignRequestDto request = new IssueAssignRequestDto();
        request.setUser_id("user1");
        request.setPriority(Priority.CRITICAL);

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.TESTER));

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/assign")
                        .param("id", id)
                        .param("pw", pw)
                        .param("projectId", String.valueOf(projectId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAssignIssueBadRequest() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        Long issueId = 1L;
        IssueAssignRequestDto request = new IssueAssignRequestDto();
        request.setUser_id("user1");
        request.setPriority(Priority.CRITICAL);

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.PL));
        when(issueService.assignIssue(projectId, issueId, request.getUser_id(), request.getPriority())).thenReturn(false);

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/assign")
                        .param("id", id)
                        .param("pw", pw)
                        .param("projectId", String.valueOf(projectId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAssignIssueSuccess() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();
        String id = "testuser";
        String pw = "password";
        Long projectId = 1L;
        Long issueId = 1L;
        IssueAssignRequestDto request = new IssueAssignRequestDto();
        request.setUser_id("user1");
        request.setPriority(Priority.CRITICAL);

        when(memberService.login(id, pw)).thenReturn(true);
        when(memberProjectService.getRole(id, projectId)).thenReturn(Optional.of(Role.PL));
        when(issueService.assignIssue(projectId, issueId, request.getUser_id(), request.getPriority())).thenReturn(true);

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/assign")
                        .param("id", id)
                        .param("pw", pw)
                        .param("projectId", String.valueOf(projectId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    void updateIssueContent_Success() throws Exception {
        // Given
        String memberId = "member123";
        Long projectId = 1L;
        Long issueId = 1L;
        String password = "password";
        IssueCreateRequestDto requestDto = new IssueCreateRequestDto("New Title", "New Description");

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.of(Role.TESTER));
        when(issueService.updateIssue(memberId, projectId, issueId, requestDto.getTitle(), requestDto.getDescription())).thenReturn(true);

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/content")
                        .param("projectId", projectId.toString())
                        .param("id", memberId)
                        .param("pw", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Title\",\"description\":\"New Description\"}"))

                // Then
                .andExpect(status().isOk());
    }

    @Test
    void updateIssueContent_Unauthorized() throws Exception {
        // Given
        String memberId = "member123";
        Long projectId = 1L;
        Long issueId = 1L;
        String password = "wrongPassword";
        IssueCreateRequestDto requestDto = new IssueCreateRequestDto("New Title", "New Description");

        when(memberService.login(memberId, password)).thenReturn(false);

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/content")
                        .param("projectId", projectId.toString())
                        .param("id", memberId)
                        .param("pw", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Title\",\"description\":\"New Description\"}"))

                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateIssueContent_Forbidden() throws Exception {
        // Given
        String memberId = "member123";
        Long projectId = 1L;
        Long issueId = 1L;
        String password = "password";
        IssueCreateRequestDto requestDto = new IssueCreateRequestDto("New Title", "New Description");

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.empty());

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/content")
                        .param("projectId", projectId.toString())
                        .param("id", memberId)
                        .param("pw", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Title\",\"description\":\"New Description\"}"))

                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    void updateIssueContent_BadRequest() throws Exception {
        // Given
        String memberId = "member123";
        Long projectId = 1L;
        Long issueId = 1L;
        String password = "password";
        IssueCreateRequestDto requestDto = new IssueCreateRequestDto("New Title", "New Description");

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.of(Role.TESTER));
        when(issueService.updateIssue(memberId, projectId, issueId, requestDto.getTitle(), requestDto.getDescription())).thenReturn(false);

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/content")
                        .param("projectId", projectId.toString())
                        .param("id", memberId)
                        .param("pw", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Title\",\"description\":\"New Description\"}"))

                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateIssueState_Success() throws Exception {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        String password = "password";
        State newState = State.RESOLVED;
        IssueStateRequest request = new IssueStateRequest();
        request.setState(newState);

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.of(Role.PL));
        when(issueService.updateIssueState(projectId, issueId, memberId, Role.PL, newState)).thenReturn(true);

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/state")
                        .param("id", memberId)
                        .param("pw", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"RESOLVED\"}"))

                // Then
                .andExpect(status().isOk());
    }

    @Test
    void updateIssueState_Unauthorized() throws Exception {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        String password = "wrongPassword";
        State newState = State.RESOLVED;
        IssueStateRequest request = new IssueStateRequest();
        request.setState(newState);

        when(memberService.login(memberId, password)).thenReturn(false);

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/state")
                        .param("id", memberId)
                        .param("pw", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"RESOLVED\"}"))

                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateIssueState_Forbidden() throws Exception {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        String password = "password";
        State newState = State.RESOLVED;
        IssueStateRequest request = new IssueStateRequest();
        request.setState(newState);

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.empty());

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/state")
                        .param("id", memberId)
                        .param("pw", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"RESOLVED\"}"))

                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    void updateIssueState_BadRequest() throws Exception {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        String password = "password";
        State newState = State.RESOLVED;
        IssueStateRequest request = new IssueStateRequest();
        request.setState(newState);

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.of(Role.PL));
        when(issueService.updateIssueState(projectId, issueId, memberId, Role.PL, newState)).thenReturn(false);

        // When
        mockMvc.perform(put("/project/" + projectId + "/issue/" + issueId + "/state")
                        .param("id", memberId)
                        .param("pw", password)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"state\":\"RESOLVED\"}"))

                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteIssue_Success() throws Exception {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        String password = "password";

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.of(Role.PL));
        when(issueService.deleteIssue(projectId, issueId)).thenReturn(true);

        // When
        mockMvc.perform(delete("/project/" + projectId + "/issue/" + issueId)
                        .param("id", memberId)
                        .param("pw", password))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    void deleteIssue_Unauthorized() throws Exception {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        String password = "wrongPassword";

        when(memberService.login(memberId, password)).thenReturn(false);

        // When
        mockMvc.perform(delete("/project/" + projectId + "/issue/" + issueId)
                        .param("id", memberId)
                        .param("pw", password))
                // Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteIssue_Forbidden_NoRole() throws Exception {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        String password = "password";

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.empty());

        // When
        mockMvc.perform(delete("/project/" + projectId + "/issue/" + issueId)
                        .param("id", memberId)
                        .param("pw", password))
                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteIssue_Forbidden_NotPL() throws Exception {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        String password = "password";

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.of(Role.DEV));

        // When
        mockMvc.perform(delete("/project/" + projectId + "/issue/" + issueId)
                        .param("id", memberId)
                        .param("pw", password))
                // Then
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteIssue_BadRequest() throws Exception {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        String password = "password";

        when(memberService.login(memberId, password)).thenReturn(true);
        when(memberProjectService.getRole(memberId, projectId)).thenReturn(Optional.of(Role.PL));
        when(issueService.deleteIssue(projectId, issueId)).thenReturn(false);

        // When
        mockMvc.perform(delete("/project/" + projectId + "/issue/" + issueId)
                        .param("id", memberId)
                        .param("pw", password))
                // Then
                .andExpect(status().isBadRequest());
    }
}
