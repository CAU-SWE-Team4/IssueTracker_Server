package com.example.issuetracker_server.service.comment;

import com.example.issuetracker_server.domain.comment.Comment;
import com.example.issuetracker_server.domain.comment.CommentRepository;
import com.example.issuetracker_server.dto.comment.CommentResponseDto;
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
    public List<CommentResponseDto> findByIssueId(Long issueId) {
        List<CommentResponseDto> commentDtos = null;
        List<Comment> comments = commentRepository.findByIssueId(issueId);
        for(Comment comment : comments) {
            CommentResponseDto commentResponseDto = new CommentResponseDto();
            commentResponseDto.setComment_id(comment.getId());
            commentResponseDto.setContent(comment.getContent());
            commentResponseDto.setCreated_date(comment.getCreatedDate().toString());
            commentResponseDto.setAuthor_name(comment.getAuthor().getName());
            commentResponseDto.setAuthor_id(comment.getAuthor().getId());
            commentDtos.add(commentResponseDto);
        }

        return commentDtos;
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
