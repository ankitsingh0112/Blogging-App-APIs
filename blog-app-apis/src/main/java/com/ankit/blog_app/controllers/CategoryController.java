package com.ankit.blog_app.controllers;

import com.ankit.blog_app.Dto.CategoryDto;
import com.ankit.blog_app.Dto.UserDto;
import com.ankit.blog_app.services.CategoryService;
import com.ankit.blog_app.utils.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "javainuseapi")
public class CategoryController {
    @Autowired
    private CategoryService service;

    // POST --> create category
    @PostMapping("category")
    public ResponseEntity<CategoryDto> createCat(@Valid @RequestBody CategoryDto catDto) {
        CategoryDto createdCat = service.createCat(catDto);
        return new ResponseEntity<>(createdCat, HttpStatus.CREATED);
    }

    // PUT --> update category
    @PutMapping("category/{catId}")
    public ResponseEntity<CategoryDto> updateCat(@Valid @RequestBody CategoryDto catDto, @PathVariable Integer catId) {
        CategoryDto updatedCat = service.updateCat(catDto, catId);
        return ResponseEntity.ok(updatedCat);
    }

    // GET --> get category
    @GetMapping("category/{catId}")
    public ResponseEntity<CategoryDto> getCat(@PathVariable Integer catId) {
        return ResponseEntity.ok(service.getCatById(catId));
    }

    @GetMapping("categories")
    public ResponseEntity<List<CategoryDto>> getAllCat() {
        return ResponseEntity.ok(service.getAllCat());
    }


    // DELETE --> delete category
    @DeleteMapping("category/{catId}")
    public ResponseEntity<ApiResponse> deleteCat(@PathVariable Integer catId) {
        service.deleteCat(catId);
        return new ResponseEntity<>(new ApiResponse("Category deleted with id : " + catId, true), HttpStatus.OK);
    }
}
