package com.example.issuetracker_server.service.comment;

import com.example.issuetracker_server.domain.comment.Comment;
import com.example.issuetracker_server.dto.comment.CommentResponseDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    void save(Comment comment);

    List<CommentResponseDto> findByIssueId(Long issueId);

    Optional<Comment> findById(Long commentId);

    void delete(Long commentId);


}
