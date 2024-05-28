package com.example.issuetracker_server.service.issue;

import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.issue.IssueRepository;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.member.MemberRepository;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.domain.project.ProjectRepository;
import com.example.issuetracker_server.dto.issue.IssueCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
