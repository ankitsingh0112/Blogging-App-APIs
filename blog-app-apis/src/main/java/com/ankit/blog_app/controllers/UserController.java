package com.ankit.blog_app.controllers;

import com.ankit.blog_app.Dto.UserDto;
import com.ankit.blog_app.entities.User;
import com.ankit.blog_app.exceptions.ApiException;
import com.ankit.blog_app.services.JwtService;
import com.ankit.blog_app.services.UserService;
import com.ankit.blog_app.utils.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "javainuseapi")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    // POST - create user
    @PostMapping("register")
    // to make the hibernate validator(it helps in check whether the user is sending correct value or not) work, we need to add @Valid
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto user) {
        UserDto createdUser = service.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // login
    @PostMapping("login")
    public String login(@RequestBody User user) {

        this.doAuthenticate(user.getEmail(), user.getPassword());

        return "Hello " + user.getEmail() + ", here is your token : " + jwtService.generateToken(user.getEmail());

    }

    private void doAuthenticate(String email, String password) {

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            authenticationManager.authenticate(authentication);


        } catch (BadCredentialsException e) {
            throw new ApiException(" Invalid Username or Password  !!"); // handling this in global exception handler
        }
    }


    // PUT - update user
    @PutMapping("user/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto user, @PathVariable Integer userId) {
        UserDto updatedUser = service.updateUser(user, userId);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE - delete user (only accessible to admin)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("user/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId) {
        service.deleteUser(userId);
        return new ResponseEntity<>(new ApiResponse("User deleted with id : " + userId, true), HttpStatus.OK);
    }

    // GET - get user (only accessible to admin)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("users")
    public ResponseEntity<List<UserDto>> getAllUser() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("user/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.getUserById(userId));
    }
}
