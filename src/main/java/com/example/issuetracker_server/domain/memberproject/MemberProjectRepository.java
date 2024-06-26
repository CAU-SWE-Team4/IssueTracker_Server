package com.example.issuetracker_server.domain.memberproject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberProjectRepository extends JpaRepository<MemberProject, Long> {
    Optional<MemberProject> findByMemberIdAndProjectId(String memberId, Long projectId);

    List<MemberProject> findByProjectIdAndRole(Long projectId, Role role);
    
    List<MemberProject> findByMemberId(String member_id);

    List<MemberProject> findByProjectId(Long project_id);
}
