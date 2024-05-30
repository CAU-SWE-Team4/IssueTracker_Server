package com.example.issuetracker_server.service;

import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.issue.IssueRepository;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.MemberProjectRepository;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.service.issue.IssueServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId);

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
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId);

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
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId);

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
        List<String> recommendedAssignees = issueService.getRecommendAssignee(projectId, issueId);

        // Then
        assertEquals(5, recommendedAssignees.size());
        assertTrue(recommendedAssignees.containsAll(Arrays.asList("user1", "user2", "user3", "user4", "user5")));
    }
}