package com.ankit.blog_app.Dto;

import com.ankit.blog_app.entities.Category;
import com.ankit.blog_app.entities.Comment;
import com.ankit.blog_app.entities.User;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
public class PostDto {

    private Integer postId;

    @NotBlank
    @Size(min = 10, message = "min length of category description is 10")
    private String title;
    @NotBlank
    @Size(min = 10, message = "min length of category description is 10")
    private String content;
    private String tags;
    private String image;
    private Date addedDate;

    private CategoryDto category;
    // not using Category class or User class directly because it creates infinite recursion

    private UserDto user;

    private Set<CommentDto> comments = new HashSet<>();
}
