package com.ankit.blog_app.services;

import com.ankit.blog_app.entities.User;
import com.ankit.blog_app.entities.UserPrincipal;
import com.ankit.blog_app.exceptions.ResourceNotFoundException;
import com.ankit.blog_app.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo repo;
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = repo.findByEmail(username);
        if(user == null) {
            System.out.println("Error 404 Not Found!");
            throw new ResourceNotFoundException("User : " + username, " try again ", 0);
        }

        return new UserPrincipal(user);
    }
}
