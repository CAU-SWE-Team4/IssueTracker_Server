package com.example.issuetracker_server.service.comment;

import com.example.issuetracker_server.domain.comment.Comment;
import com.example.issuetracker_server.domain.comment.CommentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    @Transactional
    public void save(Comment comment)
    {
        commentRepository.save(comment);
    }

    @Override
    public List<Comment> findByIssueId(Long issueId) {
        return commentRepository.findByIssueId(issueId);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);

    }


}
