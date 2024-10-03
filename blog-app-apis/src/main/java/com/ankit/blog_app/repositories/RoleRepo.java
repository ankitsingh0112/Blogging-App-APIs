package com.ankit.blog_app.repositories;

import com.ankit.blog_app.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Integer> {
}
