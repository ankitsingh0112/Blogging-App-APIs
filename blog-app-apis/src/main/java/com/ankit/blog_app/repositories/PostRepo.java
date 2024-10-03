package com.ankit.blog_app.repositories;

import com.ankit.blog_app.entities.Category;
import com.ankit.blog_app.entities.Post;
import com.ankit.blog_app.entities.User;
import com.ankit.blog_app.utils.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepo extends JpaRepository<Post, Integer> {
    Page<Post> findByUser(User user, Pageable pageable);
    Page<Post> findByCategory(Category category, Pageable pageable);
    List<Post> findByTagsContainingOrTitleContaining(String tag, String title);
}
