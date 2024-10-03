package com.ankit.blog_app.repositories;

import com.ankit.blog_app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
