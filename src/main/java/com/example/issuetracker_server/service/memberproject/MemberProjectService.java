package com.example.issuetracker_server.service.memberproject;

import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.domain.memberproject.MemberProject;
import com.example.issuetracker_server.domain.memberproject.Role;
import com.example.issuetracker_server.domain.project.Project;
import com.example.issuetracker_server.dto.project.ProjectRequestDto;

import java.util.List;
import java.util.Optional;

public interface MemberProjectService {

    Project getProject(Long id);

    Member getMember(String id);

    MemberProject toEntity(Long projectId, ProjectRequestDto.Member member);

    Long save(Long projectId, ProjectRequestDto.Member member);

    Optional<Role> getRole(String uId, Long pId);

    List<Project> getProjectIdByMemberId(String member_id);

    List<MemberProject> getMemberProjectByProjectId(Long project_id);

    void deleteAll(List<MemberProject> memberProjects);
}
