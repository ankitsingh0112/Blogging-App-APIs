package com.ankit.blog_app.controllers;


import com.ankit.blog_app.Dto.CommentDto;
import com.ankit.blog_app.entities.Comment;
import com.ankit.blog_app.services.CommentService;
import com.ankit.blog_app.utils.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "javainuseapi")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // POST => create comment
    @PostMapping("post/{postId}/user/{userId}/comment")
    public ResponseEntity<CommentDto> createComment(
            @RequestBody CommentDto commentDto,
            @PathVariable("postId") Integer postId,
            @PathVariable("userId") Integer userId
        ) {
        CommentDto createdComment = commentService.createComment(commentDto, postId, userId);

        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }


    // DELETE => delete comment
    @DeleteMapping("comment/{commentId}")
    public ResponseEntity<ApiResponse> createComment(
            @PathVariable Integer commentId
    ) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse("Category deleted with id : " + commentId, true), HttpStatus.OK);
    }
}
