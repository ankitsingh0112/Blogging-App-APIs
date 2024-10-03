package com.ankit.blog_app.Dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
    private Integer id;

    private String content;

    private UserDto user;
}
