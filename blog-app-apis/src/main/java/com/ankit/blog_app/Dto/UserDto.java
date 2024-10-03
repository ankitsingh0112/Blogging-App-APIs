package com.ankit.blog_app.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;
    @NotEmpty
    private String name;
    @Email(message = "Please enter a valid email address!!!")
    private String email;
    @NotEmpty
    @Size(min = 8, message = "Your password must contain more than or equal to 8 characters!!!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotEmpty
    @Size(min = 10, max = 100, message = "About must be in between 10 to 100 characters!!!")
    private String about;

    private Set<RoleDto> roles = new HashSet<>();
}
