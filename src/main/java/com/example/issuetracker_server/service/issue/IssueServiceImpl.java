package com.example.issuetracker_server.service.issue;

import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.issue.IssueRepository;
import com.example.issuetracker_server.domain.issue.Priority;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.MemberProjectRepository;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.issue.IssueCreateRequestDto;
import com.example.issuetracker_server.dto.issue.IssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;

    private final MemberRepository memberRepository;

    private final ProjectRepository projectRepository;
    private final MemberProjectRepository memberProjectRepository;

    @Override
    public boolean createIssue(Long projectId, String memberId, IssueCreateRequestDto request) {
        try {
            Optional<Project> project = projectRepository.findById(projectId);
            Optional<Member> member = memberRepository.findById(memberId);
            if (project.isEmpty() || member.isEmpty())
                return false;

            Issue issue = new Issue();
            issue.setProject(project.get());
            issue.setReporter(member.get());
            issue.setTitle(request.getTitle());
            issue.setDescription(request.getDescription());
            issueRepository.save(issue);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<IssueResponseDto> getIssues(Long projectId, String sort, String order) {
        List<Issue> issues;
        if ("state".equalsIgnoreCase(sort)) {
            if ("asc".equalsIgnoreCase(order))
                issues = issueRepository.findByProjectIdOrderByStateAsc(projectId);
            else
                issues = issueRepository.findByProjectIdOrderByStateDesc(projectId);
        } else if ("title".equalsIgnoreCase(sort)) {
            if ("asc".equalsIgnoreCase(order))
                issues = issueRepository.findByProjectIdOrderByTitleAsc(projectId);
            else
                issues = issueRepository.findByProjectIdOrderByTitleDesc(projectId);
        } else if ("reportedDate".equalsIgnoreCase(sort)) {
            if ("asc".equalsIgnoreCase(order))
                issues = issueRepository.findByProjectIdOrderByCreatedDateAsc(projectId);
            else
                issues = issueRepository.findByProjectIdOrderByCreatedDateDesc(projectId);
        } else
            issues = issueRepository.findByProjectIdOrderByTitleAsc(projectId); // Default sorting

        return issues.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private IssueResponseDto toDto(Issue issue) {
        return IssueResponseDto.builder()
                .id(issue.getId())
                .projectId(issue.getProject().getId())
                .title(issue.getTitle())
                .description(issue.getDescription())
                .reporterId(issue.getReporter().getId())
                .assigneeId(issue.getAssignee() != null ? issue.getAssignee().getId() : null)
                .fixerId(issue.getFixer() != null ? issue.getFixer().getId() : null)
                .priority(issue.getPriority())
                .state(issue.getState())
                .createdDate(issue.getCreatedDate().toString())
                .modifiedDate(issue.getModifiedDate().toString())
                .build();
    }

    @Override
    public Optional<IssueResponseDto> getIssue(Long projectId, Long issueId) {
        Optional<Issue> issue = issueRepository.findById(issueId);
        return issue.map(this::toDto);
    }

    @Override
    public List<String> getRecommendAssignee(Long projectId, Long issueId) {
        List<Member> devs = memberProjectRepository.findByProjectIdAndRole(projectId, Role.DEV).stream()
                .map(MemberProject::getMember).toList();
        Map<Member, Long> assigneeCount = devs.stream()
                .collect(Collectors.toMap(member -> member, member -> 0L));

        List<Issue> issues = issueRepository.findByProjectId(projectId).stream()
                .filter(issue -> issue.getFixer() == null && issue.getAssignee() != null)
                .toList();

        // 이슈의 assignee에 따라 assigneeCount 맵의 값을 증가
        issues.forEach(issue -> {
            Member assignee = issue.getAssignee();
            assigneeCount.put(assignee, assigneeCount.get(assignee) + 1);
        });

        // 결과를 이슈 수에 따라 오름차순으로 정렬
        List<Map.Entry<Member, Long>> sortedAssigneeList = assigneeCount.entrySet().stream()
                .sorted(Map.Entry.<Member, Long>comparingByValue()
                        .thenComparing(entry -> entry.getKey().getId()))
                .toList();

        // 이슈 수가 가장 적은 5명의 String id 추출
        return sortedAssigneeList.stream()
                .limit(5)
                .map(entry -> entry.getKey().getId()) // Assignee의 id를 String으로 변환
                .collect(Collectors.toList());
    }

    @Override
    public boolean assignIssue(Long projectId, Long issueId, String memberId, Priority priority) {
        Optional<MemberProject> memberProject = memberProjectRepository.findByMemberIdAndProjectId(memberId, projectId);
        if (memberProject.isEmpty() || memberProject.get().getRole() != Role.DEV)
            return false;
        Member assignee = memberProject.get().getMember();

        Optional<Issue> issue = issueRepository.findById(issueId);
        if (issue.isEmpty())
            return false;

        issue.get().setAssignee(assignee);
        issue.get().setPriority(priority);
        issueRepository.save(issue.get());
        return true;
    }
}
