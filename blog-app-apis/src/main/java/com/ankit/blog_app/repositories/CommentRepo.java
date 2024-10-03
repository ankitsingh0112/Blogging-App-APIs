package com.ankit.blog_app.repositories;

import com.ankit.blog_app.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

}
