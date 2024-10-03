package com.ankit.blog_app.services;

import com.ankit.blog_app.Dto.CommentDto;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId);

    void deleteComment(Integer commentId);
}
