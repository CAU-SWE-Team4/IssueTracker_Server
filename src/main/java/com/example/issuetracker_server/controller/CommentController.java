package com.example.issuetracker_server.controller;

import com.example.issuetracker_server.domain.comment.Comment;
import com.example.issuetracker_server.domain.issue.Issue;
import com.example.issuetracker_server.domain.member.Member;
import com.example.issuetracker_server.dto.comment.CommentRequestDto;
import com.example.issuetracker_server.dto.comment.CommentResponseDto;
import com.example.issuetracker_server.dto.member.MemberInfoDto;
import com.example.issuetracker_server.service.comment.CommentService;
import com.example.issuetracker_server.service.comment.CommentServiceImpl;
import com.example.issuetracker_server.service.issue.IssueServiceImpl;
import com.example.issuetracker_server.service.member.MemberService;
import com.example.issuetracker_server.service.member.MemberServiceImpl;
import com.example.issuetracker_server.service.project.ProjectService;
import com.example.issuetracker_server.service.project.ProjectServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/project/{projectId}/issue/{issueId}/comment")
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private MemberServiceImpl memberService;

    @Autowired
    private ProjectServiceImpl projectService;

    @Autowired
    private IssueServiceImpl issueService;

    @PostMapping
    public ResponseEntity<?> createComment(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw, @RequestBody CommentRequestDto requestDto) {
        if(!memberService.login(id, pw)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!memberService.isMemberOfProject(projectId, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Member> member = memberService.getMember(id);
        Optional<Issue> issue = issueService.getIssue(issueId);

        if(member.isPresent() && issue.isPresent())
        {
            Comment comment = new Comment();
            comment.setContent(requestDto.getContent());
            comment.setAuthor(member.get());
            comment.setIssue(issue.get());
            comment.setCreatedDate(LocalDateTime.now());
            commentService.save(comment);

            return ResponseEntity.status(HttpStatus.OK).build();
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @GetMapping
    public ResponseEntity<?> getComments(@PathVariable Long projectId, @PathVariable Long issueId, @RequestParam String id, @RequestParam String pw)
    {
        if(!memberService.login(id, pw)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!memberService.isMemberOfProject(projectId, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<CommentResponseDto> comments = commentService.findByIssueId(issueId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long projectId, @PathVariable Long issueId, @PathVariable Long commentId, @RequestParam String id, @RequestParam String pw, @RequestBody CommentRequestDto requestDto)
    {
        if(!memberService.login(id, pw)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!memberService.isMemberOfProject(projectId, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Comment> optionalComment = commentService.findById(commentId);
        if(optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if(!comment.getAuthor().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            comment.setContent(requestDto.getContent());
            commentService.save(comment);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long projectId, @PathVariable Long issueId, @PathVariable Long commentId, @RequestParam String id, @RequestParam String pw) {
        if(!memberService.login(id, pw)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(!memberService.isMemberOfProject(projectId, id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<Comment> optionalComment = commentService.findById(commentId);
        if(optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if(!comment.getAuthor().getId().equals(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            commentService.delete(commentId);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }



}
