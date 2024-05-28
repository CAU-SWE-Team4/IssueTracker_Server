package com.example.issuetracker_server.service.issue;

import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.issue.IssueRepository;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.issue.IssueCreateRequestDto;
import com.example.issuetracker_server.dto.issue.IssueResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;

    private final MemberRepository memberRepository;

    private final ProjectRepository projectRepository;

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
}
