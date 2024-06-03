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
import com.example.issuetracker_server.dto.issue.IssueStatisticResponseDto;
import com.example.issuetracker_server.service.issue.IssueServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
                .priority(Priority.CRITICAL)
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
                .priority(Priority.MAJOR)
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
                .priority(Priority.MINOR)
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
                .priority(Priority.MAJOR)
                .state(State.NEW)
                .build();

        when(issueRepository.findByProjectIdAndReporterIdContainingIgnoreCase(projectId, filterValue))
                .thenReturn(Arrays.asList(issue1, issue2));

        List<IssueResponseDto> result = issueService.getIssues(projectId, filterBy, filterValue);

        assertEquals(2, result.size());
        verify(issueRepository, times(1)).findByProjectIdAndReporterIdContainingIgnoreCase(projectId, filterValue);
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
                .priority(Priority.MINOR)
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
                .priority(Priority.MAJOR)
                .state(State.NEW)
                .build();

        when(issueRepository.findByProjectIdAndAssigneeIdContainingIgnoreCase(projectId, filterValue))
                .thenReturn(Arrays.asList(issue1, issue2));

        List<IssueResponseDto> result = issueService.getIssues(projectId, filterBy, filterValue);

        assertEquals(2, result.size());
        verify(issueRepository, times(1)).findByProjectIdAndAssigneeIdContainingIgnoreCase(projectId, filterValue);
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
                .priority(Priority.MINOR)
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
                .priority(Priority.MAJOR)
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
                .priority(Priority.MAJOR)
                .state(State.ASSIGNED)
                .build();

        when(issueRepository.findByProjectIdAndState(projectId, State.valueOf(filterValue)))
                .thenReturn(Arrays.asList(issue1, issue2));

        List<IssueResponseDto> result = issueService.getIssues(projectId, filterBy, filterValue);

        assertEquals(2, result.size());
        verify(issueRepository, times(1)).findByProjectIdAndState(projectId, State.valueOf(filterValue));
    }

    @Test
    void testGetIssuesWithNullOrEmptyFilters() {
        Long projectId = 1L;

        Project project = new Project();
        project.setId(projectId);
        Member reporter = new Member();


        Issue issue1 = Issue.builder()
                .id(1L)
                .project(project)
                .title("issue 1")
                .description("description")
                .reporter(reporter)
                .assignee(null)
                .fixer(null)
                .priority(Priority.MINOR)
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
                .priority(Priority.MAJOR)
                .state(State.ASSIGNED)
                .build();

        Issue issue3 = Issue.builder()
                .id(3L)
                .project(project)
                .title("issue 3")
                .description("description")
                .reporter(reporter)
                .assignee(null)
                .fixer(null)
                .priority(Priority.CRITICAL)
                .state(State.CLOSED)
                .build();

        when(issueRepository.findByProjectId(projectId)).thenReturn(Arrays.asList(issue1, issue2, issue3));

        // Test with filterBy and filterValue both null
        List<IssueResponseDto> result = issueService.getIssues(projectId, null, null);
        assertEquals(3, result.size());
        verify(issueRepository, times(1)).findByProjectId(projectId);

        // Test with filterBy null and filterValue empty
        result = issueService.getIssues(projectId, null, "");
        assertEquals(3, result.size());
        verify(issueRepository, times(2)).findByProjectId(projectId);

        // Test with filterBy empty and filterValue null
        result = issueService.getIssues(projectId, "", null);
        assertEquals(3, result.size());
        verify(issueRepository, times(3)).findByProjectId(projectId);

        // Test with both filterBy and filterValue empty
        result = issueService.getIssues(projectId, "", "");
        assertEquals(3, result.size());
        verify(issueRepository, times(4)).findByProjectId(projectId);
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

        Issue issue1 = Issue.builder().assignee(member1).priority(Priority.MAJOR).build();
        Issue issue2 = Issue.builder().assignee(member1).priority(Priority.MAJOR).build();
        Issue issue3 = Issue.builder().assignee(member2).priority(Priority.MAJOR).build();
        Issue issue4 = Issue.builder().assignee(member3).priority(Priority.MAJOR).build();
        Issue issue5 = Issue.builder().assignee(member4).priority(Priority.MAJOR).build();
        Issue issue6 = Issue.builder().assignee(member4).priority(Priority.MAJOR).build();
        Issue issue7 = Issue.builder().assignee(member5).priority(Priority.MAJOR).build();

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

        Issue issue1 = Issue.builder().assignee(member1).priority(Priority.MAJOR).build();
        Issue issue2 = Issue.builder().assignee(member2).priority(Priority.MAJOR).build();
        Issue issue3 = Issue.builder().assignee(member3).priority(Priority.MAJOR).build();
        Issue issue4 = Issue.builder().assignee(member4).priority(Priority.MAJOR).build();

        List<Issue> issues = Arrays.asList(issue1, issue2, issue3, issue4);
        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId).get("dev_ids");

        // Then
        assertEquals(4, recommendedAssignees.size());
        assertTrue(recommendedAssignees.containsAll(Arrays.asList("user1", "user2", "user3", "user4")));
    }

    @Test
    public void testGetRecommendAssigneeDiffPriority() {
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

        Issue issue1 = Issue.builder().assignee(member1).priority(Priority.MAJOR).build();
        Issue issue2 = Issue.builder().assignee(member1).priority(Priority.CRITICAL).build();
        Issue issue3 = Issue.builder().assignee(member2).priority(Priority.MINOR).build();
        Issue issue4 = Issue.builder().assignee(member3).priority(Priority.TRIVIAL).build();
        Issue issue5 = Issue.builder().assignee(member4).priority(Priority.BLOCKER).build();
        Issue issue6 = Issue.builder().assignee(member4).priority(Priority.MAJOR).build();
        Issue issue7 = Issue.builder().assignee(member5).priority(Priority.MAJOR).build();

        List<Issue> issues = Arrays.asList(issue1, issue2, issue3, issue4, issue5, issue6, issue7);
        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId).get("dev_ids");

        // Then
        assertEquals(5, recommendedAssignees.size());
        assertEquals("user6", recommendedAssignees.get(0));
        assertEquals("user3", recommendedAssignees.get(1));
        assertEquals("user2", recommendedAssignees.get(2));
        assertEquals("user5", recommendedAssignees.get(3));
        assertEquals("user1", recommendedAssignees.get(4));
//        Member ID: user6, Issue Count: 0
//        Member ID: user3, Issue Count: 1
//        Member ID: user2, Issue Count: 2
//        Member ID: user5, Issue Count: 3
//        Member ID: user1, Issue Count: 7
//        Member ID: user4, Issue Count: 8
    }

    @Test
    public void testGetRecommendAssigneeDiffPriorityBig() {
        // Given
        Long projectId = 1L;
        Long issueId = 1L;

        List<Member> members = new ArrayList<>();
        List<MemberProject> memberProjects = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Member member = Member.builder().id("user" + i).build();
            members.add(member);
            memberProjects.add(MemberProject.builder().role(Role.DEV).member(member).build());
        }

        when(memberProjectRepository.findByProjectIdAndRole(projectId, Role.DEV)).thenReturn(memberProjects);

        List<Issue> issues = new ArrayList<>();
        Priority[] priorities = Priority.values();
        for (int i = 0; i < 50; i++) {
            Member assignee = members.get(i % 20);
            Priority priority = priorities[i % priorities.length];
            issues.add(Issue.builder().assignee(assignee).priority(priority).build());
        }

        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId).get("dev_ids");

        // Then
        System.out.println("Recommended Assignees: " + recommendedAssignees);
        assertEquals(5, recommendedAssignees.size());
        // 각 유저의 이슈 수를 확인하여 예상 순서대로 추천되는지 확인
        Map<String, Long> assigneeIssueCount = issues.stream()
                .collect(Collectors.groupingBy(issue -> issue.getAssignee().getId(), Collectors.counting()));

        List<Map.Entry<String, Long>> sortedAssignees = assigneeIssueCount.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue()
                        .thenComparing(Map.Entry::getKey))
                .toList();

        // Then
        assertEquals(5, recommendedAssignees.size());
        assertEquals("user15", recommendedAssignees.get(0));
        assertEquals("user20", recommendedAssignees.get(1));
        assertEquals("user10", recommendedAssignees.get(2));
        assertEquals("user5", recommendedAssignees.get(3));
        assertEquals("user14", recommendedAssignees.get(4));

//        Member ID: user15, Score: 2
//        Member ID: user20, Score: 2
//        Member ID: user10, Score: 3
//        Member ID: user5, Score: 3
//        Member ID: user14, Score: 4
//        Member ID: user19, Score: 4
//        Member ID: user13, Score: 6
//        Member ID: user18, Score: 6
//        Member ID: user4, Score: 6
//        Member ID: user9, Score: 6
//        Member ID: user12, Score: 8
//        Member ID: user17, Score: 8
//        Member ID: user3, Score: 9
//        Member ID: user8, Score: 9
//        Member ID: user11, Score: 10
//        Member ID: user16, Score: 10
//        Member ID: user2, Score: 12
//        Member ID: user7, Score: 12
//        Member ID: user1, Score: 15
//        Member ID: user6, Score: 15
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

        Issue issue1 = Issue.builder().assignee(member1).priority(Priority.MAJOR).build();
        Issue issue2 = Issue.builder().assignee(member1).priority(Priority.MAJOR).build();
        Issue issue3 = Issue.builder().assignee(member2).priority(Priority.MAJOR).build();
        Issue issue4 = Issue.builder().assignee(member3).priority(Priority.MAJOR).build();

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
        mockAssignee.setId("dev123");
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
        mockReporter.setId("tester123");
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

    @Test
    public void testGetStatistic() {
        // Given
        Long projectId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Issue issueTodayClosed = new Issue();
        issueTodayClosed.setTitle("Issue Today Closed");
        issueTodayClosed.setDescription("Description");
        issueTodayClosed.setPriority(Priority.MAJOR);
        issueTodayClosed.setState(State.CLOSED);
        issueTodayClosed.setCreatedDate(LocalDateTime.now());

        Issue issueTodayOpen = new Issue();
        issueTodayOpen.setTitle("Issue Today Open");
        issueTodayOpen.setDescription("Description");
        issueTodayOpen.setPriority(Priority.CRITICAL);
        issueTodayOpen.setState(State.NEW);
        issueTodayOpen.setCreatedDate(LocalDateTime.now());

        Issue issueMonth = new Issue();
        issueMonth.setTitle("Issue Month");
        issueMonth.setDescription("Description");
        issueMonth.setPriority(Priority.MINOR);
        issueMonth.setState(State.NEW);
        issueMonth.setCreatedDate(now.minusDays(31));

        Issue issueOld = new Issue();
        issueOld.setTitle("Issue Old");
        issueOld.setDescription("Description");
        issueOld.setPriority(Priority.MINOR);
        issueOld.setState(State.CLOSED);
        issueOld.setCreatedDate(now.minusYears(1));

        List<Issue> issues = Arrays.asList(issueTodayClosed, issueTodayOpen, issueMonth, issueOld);

        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        IssueStatisticResponseDto statistics = issueService.getStatistic(projectId);

        // Then
        assertEquals(2, statistics.getDay_issues());
        assertEquals(2, statistics.getMonth_issues());
        assertEquals(4, statistics.getTotal_issues());
        assertEquals(2, statistics.getClosed_issues());
    }

    @Test
    public void testGetStatisticWithNoIssues() {
        // Given
        Long projectId = 2L;

        List<Issue> issues = Collections.emptyList();

        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        IssueStatisticResponseDto statistics = issueService.getStatistic(projectId);

        // Then
        assertEquals(0, statistics.getDay_issues());
        assertEquals(0, statistics.getMonth_issues());
        assertEquals(0, statistics.getTotal_issues());
        assertEquals(0, statistics.getClosed_issues());
    }

    @Test
    public void testGetStatisticWithMixedIssues() {
        // Given
        Long projectId = 3L;
        LocalDateTime now = LocalDateTime.now();

        Issue issueTodayClosed = new Issue();
        issueTodayClosed.setTitle("Issue Today Closed");
        issueTodayClosed.setDescription("Description");
        issueTodayClosed.setPriority(Priority.CRITICAL);
        issueTodayClosed.setState(State.CLOSED);
        issueTodayClosed.setCreatedDate(LocalDateTime.now());

        Issue issueWeekOpen = new Issue();
        issueWeekOpen.setTitle("Issue Week Open");
        issueWeekOpen.setDescription("Description");
        issueWeekOpen.setPriority(Priority.MAJOR);
        issueWeekOpen.setState(State.NEW);
        issueWeekOpen.setCreatedDate(now.minusDays(31));

        Issue issueMonthClosed = new Issue();
        issueMonthClosed.setTitle("Issue Month Closed");
        issueMonthClosed.setDescription("Description");
        issueMonthClosed.setPriority(Priority.MINOR);
        issueMonthClosed.setState(State.CLOSED);
        issueMonthClosed.setCreatedDate(now.minusDays(62));

        List<Issue> issues = Arrays.asList(issueTodayClosed, issueWeekOpen, issueMonthClosed);

        when(issueRepository.findByProjectId(projectId)).thenReturn(issues);

        // When
        IssueStatisticResponseDto statistics = issueService.getStatistic(projectId);

        // Then
        assertEquals(1, statistics.getDay_issues());
        assertEquals(1, statistics.getMonth_issues());
        assertEquals(3, statistics.getTotal_issues());
        assertEquals(2, statistics.getClosed_issues());
    }

}
