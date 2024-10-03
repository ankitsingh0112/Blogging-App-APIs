package com.ankit.blog_app.controllers;


import com.ankit.blog_app.Dto.PostDto;
import com.ankit.blog_app.services.FileService;
import com.ankit.blog_app.services.PostService;
import com.ankit.blog_app.utils.ApiResponse;
import com.ankit.blog_app.utils.AppConstant;
import com.ankit.blog_app.utils.PostResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
public class PostController {

    @Autowired
    private PostService service;

    // image related
    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


    // POST --> creating new post
    @PostMapping("user/{userId}/category/{catId}/post")
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto, @PathVariable Integer userId, @PathVariable Integer catId) {
        PostDto createdPost = service.createPost(postDto, userId, catId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // GET --> fetching post by user
    @GetMapping("user/{userId}/posts")
    public ResponseEntity<PostResponse> getPostByUser(
            @PathVariable Integer userId,
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "6", required = false) Integer pageSize) {
        return new ResponseEntity<>(service.getPostsByUser(userId, pageNumber, pageSize), HttpStatus.OK);
    }

    // GET --> fetching post by Category
    @GetMapping("category/{catId}/posts")
    public ResponseEntity<PostResponse> getPostByCategory(
            @PathVariable Integer catId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize) {
        return new ResponseEntity<>(service.getPostsByCategory(catId, pageNumber, pageSize), HttpStatus.OK);
    }

    // GET --> fetching post by id
    @GetMapping("post/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
        return ResponseEntity.ok(service.getPostById(postId));
    }

    // GET --> fetching all post (with pagination)
    @GetMapping("posts")
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.SORT_DIR, required = false) String sortDir
            ) {
        return ResponseEntity.ok(service.getAllPost(pageNumber, pageSize, sortBy, sortDir));
    }


    // searching any tag
    @GetMapping("posts/keyword/{keyword}")
    public ResponseEntity<List<PostDto>> searchByKeyword(@PathVariable("keyword") String keyword) {
        return ResponseEntity.ok(service.searchByTag(keyword));
    }


    // PUT --> updating post
    @PutMapping("post/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
        return ResponseEntity.ok(service.updatePost(postDto, postId));
    }

    // DELETE --> deleting post
    @DeleteMapping("post/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
        service.deletePost(postId);
        return new ResponseEntity<>(new ApiResponse("Category deleted with id : " + postId, true), HttpStatus.OK);
    }


    // post image upload
    @PostMapping("post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image")MultipartFile image,
            @PathVariable Integer postId
            ) throws IOException {
        PostDto postDto = service.getPostById(postId);
        String fileName = this.fileService.uploadImage(path, image);
        postDto.setImage(fileName);
        PostDto updatedPost = service.updatePost(postDto, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }


    // method to serve file
    @GetMapping(value = "post/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable("imageName") String imageName, HttpServletResponse response) throws IOException {
        InputStream resource = this.fileService.getResource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }

}
