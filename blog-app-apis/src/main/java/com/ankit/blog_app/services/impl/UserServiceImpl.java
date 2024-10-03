package com.ankit.blog_app.services.impl;

import com.ankit.blog_app.entities.Role;
import com.ankit.blog_app.entities.User;
import com.ankit.blog_app.exceptions.ResourceNotFoundException;
import com.ankit.blog_app.Dto.UserDto;
import com.ankit.blog_app.repositories.RoleRepo;
import com.ankit.blog_app.repositories.UserRepo;
import com.ankit.blog_app.services.UserService;
import com.ankit.blog_app.utils.AppConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private ModelMapper modelMapper;


    // this will help in encrypting the password
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    private RoleRepo roleRepo;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.dtoToUser(userDto);
        //encoding the password
        user.setPassword(encoder.encode(user.getPassword()));

        // specifying the role(by default user)

        Optional<Role> roleOptional = roleRepo.findById(AppConstant.NORMAL_USER);
        boolean check = roleOptional.isPresent();
        Role role = null;
        if(check)
            role = roleOptional.get();
        else
            System.out.println("error");
        user.getRoles().add(role);

        User savedUser = this.repo.save(user);
        return this.userToDto(savedUser);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Integer userId) {
        User user = this.repo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());

        User updatedUser = repo.save(user);
        return this.userToDto(updatedUser);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        User user = this.repo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

        return this.userToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = this.repo.findAll();

        List<UserDto> usersDto = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());

        return usersDto;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = this.repo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", " Id ", userId));

        this.repo.delete(user);
    }

    // UserDto to User conversion
    private User dtoToUser(UserDto userDto) {
        /*
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        return user;
        */
        // instead of manual conversion we can use model mapper library
        User user = this.modelMapper.map(userDto, User.class);
        return user;
    }

    // User to UserDto conversion
    private UserDto userToDto(User user) {
        /*
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setAbout(user.getAbout());
        return userDto;
         */
        // instead of manual conversion we can use model mapper library
        UserDto userDto = this.modelMapper.map(user, UserDto.class);
        return userDto;
    }

}
