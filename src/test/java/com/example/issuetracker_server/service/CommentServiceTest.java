package com.example.issuetracker_server.service;

import com.example.issuetracker_server.domain.comment.Comment;
import com.example.issuetracker_server.domain.comment.CommentRepository;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.dto.comment.CommentResponseDto;
import com.example.issuetracker_server.service.comment.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setContent("Test Comment");

        commentService.save(comment);

        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testFindByIssueId() {
        Comment comment1 = new Comment();
        comment1.setId(1L);
        comment1.setContent("Comment 1");
        comment1.setCreatedDate(LocalDateTime.now());
        Member member1 = new Member();
        member1.setId("user1");
        member1.setName("User One");
        comment1.setAuthor(member1);

        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setContent("Comment 2");
        comment2.setCreatedDate(LocalDateTime.now());
        Member member2 = new Member();
        member2.setId("user2");
        member2.setName("User Two");
        comment2.setAuthor(member2);

        List<Comment> comments = Arrays.asList(comment1, comment2);

        when(commentRepository.findByIssueId(anyLong())).thenReturn(comments);

        List<CommentResponseDto> commentDtos = commentService.findByIssueId(1L);

        assertEquals(2, commentDtos.size());
        assertEquals("Comment 1", commentDtos.get(0).getContent());
        assertEquals("user1", commentDtos.get(0).getAuthor_id());
        assertEquals("User One", commentDtos.get(0).getAuthor_name());

        assertEquals("Comment 2", commentDtos.get(1).getContent());
        assertEquals("user2", commentDtos.get(1).getAuthor_id());
        assertEquals("User Two", commentDtos.get(1).getAuthor_name());

        verify(commentRepository, times(1)).findByIssueId(anyLong());
    }
}
