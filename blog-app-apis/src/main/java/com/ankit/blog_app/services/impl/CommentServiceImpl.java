package com.ankit.blog_app.services.impl;

import com.ankit.blog_app.Dto.CommentDto;
import com.ankit.blog_app.entities.Comment;
import com.ankit.blog_app.entities.Post;
import com.ankit.blog_app.entities.User;
import com.ankit.blog_app.exceptions.ResourceNotFoundException;
import com.ankit.blog_app.repositories.CommentRepo;
import com.ankit.blog_app.repositories.PostRepo;
import com.ankit.blog_app.repositories.UserRepo;
import com.ankit.blog_app.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId, Integer userId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(()-> new ResourceNotFoundException("Post", " Id ", postId));

        User user = userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", " Id ", userId));

        Comment comment = modelMapper.map(commentDto, Comment.class);

        comment.setPost(post);
        comment.setUser(user);

        Comment savedComment = commentRepo.save(comment);
        return modelMapper.map(comment, CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {

        Comment comment = commentRepo.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment", " Id ", commentId));

        commentRepo.delete(comment);

    }
}
