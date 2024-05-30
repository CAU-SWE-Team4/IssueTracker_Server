package com.example.issuetracker_server.dto.comment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {

    public Long comment_id;
    public String author_id;
    public String author_name;
    public String content;
    public String created_date;
}
