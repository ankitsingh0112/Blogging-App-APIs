package com.ankit.blog_app.services.impl;

import com.ankit.blog_app.Dto.CategoryDto;
import com.ankit.blog_app.entities.Category;
import com.ankit.blog_app.exceptions.ResourceNotFoundException;
import com.ankit.blog_app.repositories.CategoryRepo;
import com.ankit.blog_app.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepo repo;

    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CategoryDto createCat(CategoryDto catDto) {
        Category cat = this.modelMapper.map(catDto, Category.class);
        Category savedCat = this.repo.save(cat);
        return this.modelMapper.map(savedCat, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCat(CategoryDto catDto, Integer catId) {
        Category cat = repo.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", " Id ", catId));

        cat.setCatTitle(catDto.getCatTitle());
        cat.setCatDesc(catDto.getCatDesc());

        Category updatedCat = repo.save(cat);
        return this.modelMapper.map(updatedCat, CategoryDto.class);
    }

    @Override
    public void deleteCat(Integer catId) {
        Category cat = repo.findById(catId).orElseThrow(() -> new ResourceNotFoundException("Category", " Id ", catId));
        repo.delete(cat);
    }

    @Override
    public CategoryDto getCatById(Integer catId) {

        Category cat = repo.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", " Id ", catId));

        return this.modelMapper.map(cat, CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCat() {
        List<Category> cats = repo.findAll();

        List<CategoryDto> catsDto = cats.stream()
                .map(cat -> this.modelMapper.map(cat, CategoryDto.class))
                .collect(Collectors.toList());

        return catsDto;

    }

}
