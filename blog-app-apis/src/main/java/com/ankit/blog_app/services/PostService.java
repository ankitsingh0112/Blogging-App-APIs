package com.ankit.blog_app.services;

import com.ankit.blog_app.Dto.PostDto;
import com.ankit.blog_app.utils.PostResponse;

import java.util.List;

public interface PostService {
    // POST --> create post
    PostDto createPost(PostDto postDto, Integer userId, Integer catId);

    // PUT --> update post
    PostDto updatePost(PostDto postDto, Integer postId);

    // DELETE --> delete post
    void deletePost(Integer postId);

    // GET --> fetch all post
    // for pagination and sorting we are passing arguments otherwise no need to pass
    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    // GET --> fetch single post
    PostDto getPostById(Integer postId);

    // get all post by category
    PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize);

    // get all post by user
    PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize);

    // search post by tags
    List<PostDto> searchByTag(String keyword);
}
