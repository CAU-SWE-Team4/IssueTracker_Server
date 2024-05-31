package com.example.issuetracker_server.service;

import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.issue.IssueRepository;
import com.example.issuetracker_server.domain.issue.Priority;
import com.example.issuetracker_server.domain.issue.State;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.MemberProjectRepository;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.dto.issue.IssueResponseDto;
import com.example.issuetracker_server.service.issue.IssueServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class IssueServiceTest {

    @Mock
    private MemberProjectRepository memberProjectRepository;

    @Mock
    private IssueRepository issueRepository;

    @InjectMocks
    private IssueServiceImpl issueService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIssuesByTitle() {
        Long projectId = 1L;
        String filterBy = "title";
        String filterValue = "bug";

        Project project = new Project();
        Member reporter = new Member();

        Issue issue1 = Issue.builder()
                .id(1L)
                .project(project)
                .title("bug fix")
                .description("description")
                .reporter(reporter)
                .assignee(null)
                .fixer(null)
                .priority(Priority.HIGH)
                .state(State.NEW)
                .build();

        Issue issue2 = Issue.builder()
                .id(2L)
                .project(project)
                .title("bug report")
                .description("description")
                .reporter(reporter)
                .assignee(null)
                .fixer(null)
                .priority(Priority.MEDIUM)
                .state(State.NEW)
                .build();

        when(issueRepository.findByProjectIdAndTitleContainingIgnoreCase(projectId, filterValue))
                .thenReturn(Arrays.asList(issue1, issue2));

        List<IssueResponseDto> result = issueService.getIssues(projectId, filterBy, filterValue);

        assertEquals(2, result.size());
        verify(issueRepository, times(1)).findByProjectIdAndTitleContainingIgnoreCase(projectId, filterValue);
    }

    @Test
    void testGetIssuesByReporter() {
        Long projectId = 1L;
        String filterBy = "reporter";
        String filterValue = "john";

        Project project = new Project();
        Member reporter = new Member();

        Issue issue1 = Issue.builder()
                .id(1L)
                .project(project)
                .title("issue 1")
                .description("description")
                .reporter(reporter)
                .assignee(null)
                .fixer(null)
                .priority(Priority.LOW)
                .state(State.NEW)
                .build();

        Issue issue2 = Issue.builder()
                .id(2L)
                .project(project)
                .title("issue 2")
                .description("description")
                .reporter(reporter)
                .assignee(null)
                .fixer(null)
                .priority(Priority.MEDIUM)
                .state(State.NEW)
                .build();

        when(issueRepository.findByProjectIdAndReporterContainingIgnoreCase(projectId, filterValue))
                .thenReturn(Arrays.asList(issue1, issue2));

        List<IssueResponseDto> result = issueService.getIssues(projectId, filterBy, filterValue);

        assertEquals(2, result.size());
        verify(issueRepository, times(1)).findByProjectIdAndReporterContainingIgnoreCase(projectId, filterValue);
    }

    @Test
    void testGetIssuesByAssignee() {
        Long projectId = 1L;
        String filterBy = "assignee";
        String filterValue = "jane";

        Project project = new Project();
        Member assignee = new Member();

        Issue issue1 = Issue.builder()
                .id(1L)
                .project(project)
                .title("issue 1")
                .description("description")
                .reporter(new Member())
                .assignee(assignee)
                .fixer(null)
                .priority(Priority.LOW)
                .state(State.NEW)
                .build();

        Issue issue2 = Issue.builder()
                .id(2L)
                .project(project)
                .title("issue 2")
                .description("description")
                .reporter(new Member())
                .assignee(assignee)
                .fixer(null)
                .priority(Priority.MEDIUM)
                .state(State.NEW)
                .build();

        when(issueRepository.findByProjectIdAndAssigneeContainingIgnoreCase(projectId, filterValue))
                .thenReturn(Arrays.asList(issue1, issue2));

        List<IssueResponseDto> result = issueService.getIssues(projectId, filterBy, filterValue);

        assertEquals(2, result.size());
        verify(issueRepository, times(1)).findByProjectIdAndAssigneeContainingIgnoreCase(projectId, filterValue);
    }

    @Test
    void testGetIssuesByState() {
        Long projectId = 1L;
        String filterBy = "state";
        String filterValue = "NEW";

        Project project = new Project();
        Member reporter = new Member();

        Issue issue1 = Issue.builder()
                .id(1L)
                .project(project)
                .title("issue 1")
                .description("description")
                .reporter(reporter)
                .assignee(null)
                .fixer(null)
                .priority(Priority.LOW)
                .state(State.NEW)
                .build();

        Issue issue2 = Issue.builder()
                .id(2L)
                .project(project)
                .title("issue 2")
                .description("description")
                .reporter(reporter)
                .assignee(null)
                .fixer(null)
                .priority(Priority.MEDIUM)
                .state(State.NEW)
                .build();

        Issue issue3 = Issue.builder()
                .id(3L)
                .project(project)
                .title("issue 3")
                .description("description")
                .reporter(reporter)
                .assignee(null)
                .fixer(null)
                .priority(Priority.MEDIUM)
                .state(State.ASSIGNED)
                .build();

        when(issueRepository.findByProjectIdAndStateContainingIgnoreCase(projectId, filterValue))
                .thenReturn(Arrays.asList(issue1, issue2));

        List<IssueResponseDto> result = issueService.getIssues(projectId, filterBy, filterValue);

        assertEquals(2, result.size());
        verify(issueRepository, times(1)).findByProjectIdAndStateContainingIgnoreCase(projectId, filterValue);
    }

    @Test
    public void testGetRecommendAssignee() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;

        Member member1 = Member.builder().id("user1").build();
        Member member2 = Member.builder().id("user2").build();
        Member member3 = Member.builder().id("user3").build();
        Member member4 = Member.builder().id("user4").build();
        Member member5 = Member.builder().id("user5").build();
        Member member6 = Member.builder().id("user6").build();

        MemberProject mp1 = MemberProject.builder().role(Role.DEV).member(member1).build();
        MemberProject mp2 = MemberProject.builder().role(Role.DEV).member(member2).build();
        MemberProject mp3 = MemberProject.builder().role(Role.DEV).member(member3).build();
        MemberProject mp4 = MemberProject.builder().role(Role.DEV).member(member4).build();
        MemberProject mp5 = MemberProject.builder().role(Role.DEV).member(member5).build();
        MemberProject mp6 = MemberProject.builder().role(Role.DEV).member(member6).build();

        List<MemberProject> memberProjects = Arrays.asList(mp1, mp2, mp3, mp4, mp5, mp6);
        when(memberProjectRepository.findByProjectIdAndRole(projectId, Role.DEV)).thenReturn(memberProjects);

        Issue issue1 = Issue.builder().assignee(member1).build();
        Issue issue2 = Issue.builder().assignee(member1).build();
        Issue issue3 = Issue.builder().assignee(member2).build();
        Issue issue4 = Issue.builder().assignee(member3).build();
        Issue issue5 = Issue.builder().assignee(member4).build();
        Issue issue6 = Issue.builder().assignee(member4).build();
        Issue issue7 = Issue.builder().assignee(member5).build();

        List<Issue> issues = Arrays.asList(issue1, issue2, issue3, issue4, issue5, issue6, issue7);
        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId).get("dev_ids");

        // Then
        assertEquals(5, recommendedAssignees.size());
        assertEquals("user6", recommendedAssignees.get(0)); // Issue 없는 멤버
        assertEquals("user2", recommendedAssignees.get(1)); // Issue 1개인 멤버
        assertEquals("user3", recommendedAssignees.get(2)); // Issue 1개인 멤버
        assertEquals("user5", recommendedAssignees.get(3)); // Issue 1개인 멤버
        assertEquals("user1", recommendedAssignees.get(4)); // Issue 2개인 멤버
    }

    @Test
    public void testGetRecommendAssignee_AllEqualIssues() {
        // Given
        Long projectId = 2L;
        Long issueId = 2L;

        Member member1 = Member.builder().id("user1").build();
        Member member2 = Member.builder().id("user2").build();
        Member member3 = Member.builder().id("user3").build();
        Member member4 = Member.builder().id("user4").build();

        MemberProject mp1 = MemberProject.builder().role(Role.DEV).member(member1).build();
        MemberProject mp2 = MemberProject.builder().role(Role.DEV).member(member2).build();
        MemberProject mp3 = MemberProject.builder().role(Role.DEV).member(member3).build();
        MemberProject mp4 = MemberProject.builder().role(Role.DEV).member(member4).build();

        List<MemberProject> memberProjects = Arrays.asList(mp1, mp2, mp3, mp4);
        when(memberProjectRepository.findByProjectIdAndRole(projectId, Role.DEV)).thenReturn(memberProjects);

        Issue issue1 = Issue.builder().assignee(member1).build();
        Issue issue2 = Issue.builder().assignee(member2).build();
        Issue issue3 = Issue.builder().assignee(member3).build();
        Issue issue4 = Issue.builder().assignee(member4).build();

        List<Issue> issues = Arrays.asList(issue1, issue2, issue3, issue4);
        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId).get("dev_ids");

        // Then
        assertEquals(4, recommendedAssignees.size());
        assertTrue(recommendedAssignees.containsAll(Arrays.asList("user1", "user2", "user3", "user4")));
    }

    @Test
    public void testGetRecommendAssignee_SomeWithIssues() {
        // Given
        Long projectId = 3L;
        Long issueId = 3L;

        Member member1 = Member.builder().id("user1").build();
        Member member2 = Member.builder().id("user2").build();
        Member member3 = Member.builder().id("user3").build();
        Member member4 = Member.builder().id("user4").build();
        Member member5 = Member.builder().id("user5").build();

        MemberProject mp1 = MemberProject.builder().role(Role.DEV).member(member1).build();
        MemberProject mp2 = MemberProject.builder().role(Role.DEV).member(member2).build();
        MemberProject mp3 = MemberProject.builder().role(Role.DEV).member(member3).build();
        MemberProject mp4 = MemberProject.builder().role(Role.DEV).member(member4).build();
        MemberProject mp5 = MemberProject.builder().role(Role.DEV).member(member5).build();

        List<MemberProject> memberProjects = Arrays.asList(mp1, mp2, mp3, mp4, mp5);
        when(memberProjectRepository.findByProjectIdAndRole(projectId, Role.DEV)).thenReturn(memberProjects);

        Issue issue1 = Issue.builder().assignee(member1).build();
        Issue issue2 = Issue.builder().assignee(member1).build();
        Issue issue3 = Issue.builder().assignee(member2).build();
        Issue issue4 = Issue.builder().assignee(member3).build();

        List<Issue> issues = Arrays.asList(issue1, issue2, issue3, issue4);
        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId).get("dev_ids");

        // Then
        assertEquals(5, recommendedAssignees.size());
        assertEquals("user4", recommendedAssignees.get(0)); // Issue 없는 멤버
        assertEquals("user5", recommendedAssignees.get(1)); // Issue 없는 멤버
        assertEquals("user2", recommendedAssignees.get(2)); // Issue 1개인 멤버
        assertEquals("user3", recommendedAssignees.get(3)); // Issue 1개인 멤버
        assertEquals("user1", recommendedAssignees.get(4)); // Issue 2개인 멤버
    }

    @Test
    public void testGetRecommendAssignee_NoIssues() {
        // Given
        Long projectId = 4L;
        Long issueId = 4L;

        Member member1 = Member.builder().id("user1").build();
        Member member2 = Member.builder().id("user2").build();
        Member member3 = Member.builder().id("user3").build();
        Member member4 = Member.builder().id("user4").build();
        Member member5 = Member.builder().id("user5").build();
        Member member6 = Member.builder().id("user6").build();

        MemberProject mp1 = MemberProject.builder().role(Role.DEV).member(member1).build();
        MemberProject mp2 = MemberProject.builder().role(Role.DEV).member(member2).build();
        MemberProject mp3 = MemberProject.builder().role(Role.DEV).member(member3).build();
        MemberProject mp4 = MemberProject.builder().role(Role.DEV).member(member4).build();
        MemberProject mp5 = MemberProject.builder().role(Role.DEV).member(member5).build();
        MemberProject mp6 = MemberProject.builder().role(Role.DEV).member(member6).build();

        List<MemberProject> memberProjects = Arrays.asList(mp1, mp2, mp3, mp4, mp5, mp6);
        when(memberProjectRepository.findByProjectIdAndRole(projectId, Role.DEV)).thenReturn(memberProjects);

        List<Issue> issues = Arrays.asList();
        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId).get("dev_ids");

        // Then
        assertEquals(5, recommendedAssignees.size());
        assertTrue(recommendedAssignees.containsAll(Arrays.asList("user1", "user2", "user3", "user4", "user5")));
    }

    @Test
    void updateIssue_Success() {
        // Given
        String memberId = "member123";
        Long projectId = 1L;
        Long issueId = 1L;
        String title = "New Title";
        String description = "New Description";

        Issue mockIssue = new Issue();
        Project mockProject = new Project();
        mockProject.setId(projectId);
        Member mockMember = new Member();
        mockMember.setId(memberId);

        mockIssue.setProject(mockProject);
        mockIssue.setReporter(mockMember);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.updateIssue(memberId, projectId, issueId, title, description);

        // Then
        assertTrue(result);
        assertEquals(title, mockIssue.getTitle());
        assertEquals(description, mockIssue.getDescription());
        verify(issueRepository, times(1)).save(mockIssue);
    }

    @Test
    void updateIssue_IssueNotFound() {
        // Given
        String memberId = "member123";
        Long projectId = 1L;
        Long issueId = 1L;
        String title = "New Title";
        String description = "New Description";

        when(issueRepository.findById(issueId)).thenReturn(Optional.empty());

        // When
        boolean result = issueService.updateIssue(memberId, projectId, issueId, title, description);

        // Then
        assertFalse(result);
        verify(issueRepository, never()).save(any());
    }

    @Test
    void updateIssue_ProjectMismatch() {
        // Given
        String memberId = "member123";
        Long projectId = 1L;
        Long issueId = 1L;
        String title = "New Title";
        String description = "New Description";

        Issue mockIssue = new Issue();
        Project mockProject = new Project();
        mockProject.setId(2L);  // Different project ID
        Member mockMember = new Member();
        mockMember.setId(memberId);

        mockIssue.setProject(mockProject);
        mockIssue.setReporter(mockMember);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.updateIssue(memberId, projectId, issueId, title, description);

        // Then
        assertFalse(result);
        verify(issueRepository, never()).save(any());
    }

    @Test
    void updateIssue_ReporterMismatch() {
        // Given
        String memberId = "member123";
        Long projectId = 1L;
        Long issueId = 1L;
        String title = "New Title";
        String description = "New Description";

        Issue mockIssue = new Issue();
        Project mockProject = new Project();
        mockProject.setId(projectId);
        Member mockMember = new Member();
        mockMember.setId("differentMember");  // Different reporter ID

        mockIssue.setProject(mockProject);
        mockIssue.setReporter(mockMember);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.updateIssue(memberId, projectId, issueId, title, description);

        // Then
        assertFalse(result);
        verify(issueRepository, never()).save(any());
    }

    @Test
    void updateIssueState_SuccessForPL() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "pl123";
        Role role = Role.PL;
        State newState = State.RESOLVED;

        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);
        Project mockProject = new Project();
        mockProject.setId(projectId);
        mockIssue.setProject(mockProject);
        mockIssue.setState(State.NEW);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.updateIssueState(projectId, issueId, memberId, role, newState);

        // Then
        assertTrue(result);
        assertEquals(newState, mockIssue.getState());
        verify(issueRepository, times(1)).save(mockIssue);
    }

    @Test
    void updateIssueState_FailureForInvalidProject() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        Role role = Role.PL;
        State newState = State.RESOLVED;

        Issue mockIssue = new Issue();
        Project mockProject = new Project();
        mockProject.setId(2L); // Different project ID
        mockIssue.setProject(mockProject);
        mockIssue.setState(State.NEW);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.updateIssueState(projectId, issueId, memberId, role, newState);

        // Then
        assertFalse(result);
        verify(issueRepository, times(0)).save(mockIssue);
    }

    @Test
    void updateIssueState_SuccessForDev() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "dev123";
        Role role = Role.DEV;
        State newState = State.FIXED;

        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);
        Project mockProject = new Project();
        mockProject.setId(projectId);
        Member mockAssignee = new Member();
        mockAssignee.setId("differentDev123");
        mockIssue.setAssignee(mockAssignee);
        mockIssue.setProject(mockProject);
        mockIssue.setState(State.ASSIGNED);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.updateIssueState(projectId, issueId, memberId, role, newState);

        // Then
        assertTrue(result);
        assertEquals(State.FIXED, mockIssue.getState());
        verify(issueRepository, times(1)).save(mockIssue);
    }

    @Test
    void updateIssueState_SuccessForTester() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "tester123";
        Role role = Role.TESTER;
        State newState = State.RESOLVED;

        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);
        Project mockProject = new Project();
        mockProject.setId(projectId);
        Member mockReporter = new Member();
        mockReporter.setId("differentTester123");
        Member mockAssignee = new Member();
        mockAssignee.setId("assignee123");
        mockIssue.setReporter(mockReporter);
        mockIssue.setAssignee(mockAssignee);
        mockIssue.setProject(mockProject);
        mockIssue.setState(State.FIXED);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.updateIssueState(projectId, issueId, memberId, role, newState);

        // Then
        assertTrue(result);
        assertEquals(State.RESOLVED, mockIssue.getState());
        verify(issueRepository, times(1)).save(mockIssue);
    }

    @Test
    void updateIssueState_FailureForInvalidRole() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        String memberId = "member123";
        Role role = Role.DEV;
        State newState = State.RESOLVED;

        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);
        Project mockProject = new Project();
        mockProject.setId(projectId);
        mockIssue.setProject(mockProject);
        mockIssue.setState(State.NEW);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.updateIssueState(projectId, issueId, memberId, role, newState);

        // Then
        assertFalse(result);
        verify(issueRepository, times(0)).save(mockIssue);
    }

    @Test
    void deleteIssue_Success() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);
        Project mockProject = new Project();
        mockProject.setId(projectId);
        mockIssue.setProject(mockProject);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.deleteIssue(projectId, issueId);

        // Then
        assertTrue(result);
        verify(issueRepository, times(1)).delete(mockIssue);
    }

    @Test
    void deleteIssue_Failure_IssueNotFound() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;

        when(issueRepository.findById(issueId)).thenReturn(Optional.empty());

        // When
        boolean result = issueService.deleteIssue(projectId, issueId);

        // Then
        assertFalse(result);
        verify(issueRepository, times(0)).delete(any());
    }

    @Test
    void deleteIssue_Failure_ProjectIdMismatch() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;
        Issue mockIssue = new Issue();
        mockIssue.setId(issueId);
        Project mockProject = new Project();
        mockProject.setId(2L); // Different project ID
        mockIssue.setProject(mockProject);

        when(issueRepository.findById(issueId)).thenReturn(Optional.of(mockIssue));

        // When
        boolean result = issueService.deleteIssue(projectId, issueId);

        // Then
        assertFalse(result);
        verify(issueRepository, times(0)).delete(any());
    }
}
