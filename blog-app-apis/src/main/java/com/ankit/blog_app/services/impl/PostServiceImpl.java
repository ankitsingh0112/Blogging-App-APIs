package com.ankit.blog_app.services.impl;

import com.ankit.blog_app.Dto.PostDto;
import com.ankit.blog_app.entities.Category;
import com.ankit.blog_app.entities.Post;
import com.ankit.blog_app.entities.User;
import com.ankit.blog_app.exceptions.ResourceNotFoundException;
import com.ankit.blog_app.repositories.CategoryRepo;
import com.ankit.blog_app.repositories.PostRepo;
import com.ankit.blog_app.repositories.UserRepo;
import com.ankit.blog_app.services.PostService;
import com.ankit.blog_app.utils.PostResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;
    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer catId) {
        User user = userRepo.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User", " Id ", userId));

        Category category = categoryRepo.findById(catId)
                .orElseThrow(()-> new ResourceNotFoundException("Category", " Id ", catId));

        Post post = modelMapper.map(postDto, Post.class);
        post.setImage("default.png");
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);
        Post savedPost = postRepo.save(post);
        return modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", " Id ", postId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setTags(postDto.getTags());
        post.setImage(postDto.getImage());

        Post savedPost = postRepo.save(post);
        return modelMapper.map(savedPost, PostDto.class);
    }

    @Override
    public void deletePost(Integer postId) {
        postRepo.delete(postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", " Id ", postId)));
    }

    @Override
    // for pagination and sorting we are passing arguments otherwise no need to pass
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        // implementing sorting
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")) {
            sort = Sort.by(sortBy).ascending(); // by default sort in ascending(if we do not add .ascending() it will still work)
        }
        else {
            sort = Sort.by(sortBy).descending();
        }

        // pagination
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);
        Page<Post> pagePost = postRepo.findAll(p);

        List<Post> posts = pagePost.getContent();
        List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setLastPage(pagePost.isLast());
        postResponse.setTotalPages(pagePost.getTotalPages());

        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", " Id ", postId));

        return modelMapper.map(post, PostDto.class);
    }


    @Override
    public PostResponse getPostsByCategory(Integer categoryId, Integer pageNumber, Integer pageSize) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", " Id ", categoryId));

        // pagination
        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<Post> pagePost = postRepo.findByCategory(category, p);

        List<Post> posts = pagePost.getContent();
        List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setLastPage(pagePost.isLast());
        postResponse.setTotalPages(pagePost.getTotalPages());

        return postResponse;
    }

    @Override
    public PostResponse getPostsByUser(Integer userId, Integer pageNumber, Integer pageSize) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

        // pagination
        Pageable p = PageRequest.of(pageNumber, pageSize);
        Page<Post> pagePost = postRepo.findByUser(user, p);

        List<Post> posts = pagePost.getContent();
        List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setLastPage(pagePost.isLast());
        postResponse.setTotalPages(pagePost.getTotalPages());

        return postResponse;
    }

    @Override
    public List<PostDto> searchByTag(String keyword) {
        List<Post> posts = postRepo.findByTagsContainingOrTitleContaining(keyword, keyword);
        List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }
}
