package com.ankit.blog_app.Dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
    private Integer catId;
    @NotBlank
    @Size(min = 4, message = "min length of category title is 4")
    private String catTitle;
    @NotBlank
    @Size(min = 10, message = "min length of category description is 10")
    private String catDesc;
}
