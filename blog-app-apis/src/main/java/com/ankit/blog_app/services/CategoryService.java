package com.ankit.blog_app.services;

import com.ankit.blog_app.Dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    // POST --> create category
    CategoryDto createCat(CategoryDto catDto);

    // PUT --> update category
    CategoryDto updateCat(CategoryDto catDto, Integer catId);

    // DELETE --> delete category
    void deleteCat(Integer catId);

    // GET --> fetch category
    CategoryDto getCatById(Integer catId);

    List<CategoryDto> getAllCat();
}
