package com.example.issuetracker_server.domain.comment;

import com.example.issuetracker_server.domain.BaseTimeEntity;
import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.member.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id", nullable = false)
    private Issue issue;

    @Column(length = 1000, nullable = false)
    private String content;
}
