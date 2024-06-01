package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.comment.Comment;
import com.example.issuetracker_server.domain.comment.CommentRepository;
import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.dto.comment.CommentRequestDto;
import com.example.issuetracker_server.dto.comment.CommentResponseDto;
import com.example.issuetracker_server.service.comment.CommentServiceImpl;
import com.example.issuetracker_server.service.issue.IssueServiceImpl;
import com.example.issuetracker_server.service.member.MemberServiceImpl;
import com.example.issuetracker_server.service.project.ProjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CommentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentServiceImpl commentService;

    @MockBean
    private MemberServiceImpl memberService;

    @MockBean
    private ProjectServiceImpl projectService;

    @MockBean
    private IssueServiceImpl issueService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    String url = "http://localhost:" + port + "/project/";
    @Test
    public void createComment_Success() throws Exception {
        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("Test Comment");

        Member member = new Member();
        member.setId("user1");

        Issue issue = new Issue();
        issue.setId(1L);

        when(memberService.login(anyString(),anyString())).thenReturn(true);
        when(memberService.isMemberOfProject(anyLong(),anyString())).thenReturn(true);
        when(memberService.getMember(anyString())).thenReturn(Optional.of(member));
        when(issueService.getIssue(anyLong())).thenReturn(Optional.of(issue));

        mvc.perform(post(url + "1/issue/1/comment")

                .param("id", "user1")
                .param("pw","password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\":  \"Test Comment\"}"))
                .andExpect(status().isOk());
        verify(commentService, times(1)).save(any(Comment.class));
    }

    @Test
    public void createComment_Unauthorized() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(false);

        mvc.perform(post(url + "1/issue/1/comment")
                        .param("id", "user1")
                        .param("pw", "password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"Test Comment\"}"))
                .andExpect(status().isUnauthorized());

        verify(commentService, times(0)).save(any(Comment.class));
    }

    @Test
    public void createComment_Forbidden() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(memberService.isMemberOfProject(anyLong(), anyString())).thenReturn(false);

        mvc.perform(post(url + "1/issue/1/comment")
                        .param("id", "user1")
                        .param("pw", "password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"Test Comment\"}"))
                .andExpect(status().isForbidden());

        verify(commentService, times(0)).save(any(Comment.class));

    }

    @Test
    public void getComments_Success() throws Exception {
        CommentResponseDto comment1 = new CommentResponseDto();
        comment1.setComment_id(1L);
        comment1.setContent("Test Comment 1");
        comment1.setAuthor_id("user1");
        comment1.setCreated_date(String.valueOf(LocalDateTime.now()));
        comment1.setAuthor_name("username");

        CommentResponseDto comment2 = new CommentResponseDto();
        comment2.setComment_id(2L);
        comment2.setContent("Test Comment 2");
        comment2.setAuthor_id("user2");
        comment2.setCreated_date(String.valueOf(LocalDateTime.now()));
        comment2.setAuthor_name("user2name");

        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(memberService.isMemberOfProject(anyLong(), anyString())).thenReturn(true);
        when(commentService.findByIssueId(anyLong())).thenReturn((List<CommentResponseDto>) Arrays.asList(comment1, comment2));

        mvc.perform(get(url + "1/issue/1/comment")
                        .param("id", "user1")
                        .param("pw", "password"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].content").value("Test Comment 1"))
                .andExpect(jsonPath("$[1].content").value("Test Comment 2"));
    }

    @Test
    public void updateComment_Success() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(new Member("user1", "password", "User One", "ROLE_USER"));
        comment.setContent("Old Content");

        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("New Content");

        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(memberService.isMemberOfProject(anyLong(), anyString())).thenReturn(true);
        when(commentService.findById(anyLong())).thenReturn(Optional.of(comment));

        mvc.perform(put(url + "1/issue/1/comment/1")
                        .param("id", "user1")
                        .param("pw", "password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"New Content\"}"))
                .andExpect(status().isOk());

        verify(commentService, times(1)).save(any(Comment.class));
    }

    @Test
    public void updateComment_Forbidden() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(new Member("user1", "password", "User One", "ROLE_USER"));
        comment.setContent("Old Content");

        CommentRequestDto requestDto = new CommentRequestDto();
        requestDto.setContent("New Content");

        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(memberService.isMemberOfProject(anyLong(), anyString())).thenReturn(true);
        when(commentService.findById(anyLong())).thenReturn(Optional.of(comment));

        mvc.perform(put(url + "1/issue/1/comment/1")
                        .param("id", "user2")
                        .param("pw", "password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"New Content\"}"))
                .andExpect(status().isForbidden());

        verify(commentService, times(0)).save(any(Comment.class));
    }

    @Test
    public void updateComment_BadRequest() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(memberService.isMemberOfProject(anyLong(), anyString())).thenReturn(true);
        when(commentService.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(put(url + "1/issue/1/comment/1")
                        .param("id", "user1")
                        .param("pw", "password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"content\": \"New Content\"}"))
                .andExpect(status().isBadRequest());

        verify(commentService, times(0)).save(any(Comment.class));
    }

    @Test
    public void deleteComment_Success() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(new Member("user1", "password", "User One", "ROLE_USER"));

        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(memberService.isMemberOfProject(anyLong(), anyString())).thenReturn(true);
        when(commentService.findById(anyLong())).thenReturn(Optional.of(comment));

        mvc.perform(delete(url + "1/issue/1/comment/1")
                        .param("id", "user1")
                        .param("pw", "password"))
                .andExpect(status().isOk());

        verify(commentService, times(1)).delete(anyLong());
    }

    @Test
    public void deleteComment_Unauthorized() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(false);

        mvc.perform(delete(url + "1/issue/1/comment/1")
                        .param("id", "user1")
                        .param("pw", "password"))
                .andExpect(status().isUnauthorized());

        verify(commentService, times(0)).delete(anyLong());
    }

    @Test
    public void deleteComment_Forbidden_NotCommentAuthor() throws Exception {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setAuthor(new Member("user2", "password", "User Two", "ROLE_USER"));

        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(memberService.isMemberOfProject(anyLong(), anyString())).thenReturn(true);
        when(commentService.findById(anyLong())).thenReturn(Optional.of(comment));

        mvc.perform(delete(url + "1/issue/1/comment/1")
                        .param("id", "user1")
                        .param("pw", "password"))
                .andExpect(status().isForbidden());

        verify(commentService, times(0)).delete(anyLong());
    }

    @Test
    public void deleteComment_NotFound() throws Exception {
        when(memberService.login(anyString(), anyString())).thenReturn(true);
        when(memberService.isMemberOfProject(anyLong(), anyString())).thenReturn(true);
        when(commentService.findById(anyLong())).thenReturn(Optional.empty());

        mvc.perform(delete(url + "1/issue/1/comment/1")
                        .param("id", "user1")
                        .param("pw", "password"))
                .andExpect(status().isBadRequest());

        verify(commentService, times(0)).delete(anyLong());
    }
}

