package com.samia.gestion.clients.controller;

import com.samia.gestion.clients.DTO.CategoryDTO;
import com.samia.gestion.clients.DTO.ClientDTO;
import com.samia.gestion.clients.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category")
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO){
        return categoryService.createCategory(categoryDTO);
    }

    @GetMapping("/category/{id}")
    public CategoryDTO getCategoryById(@PathVariable(value = "id") Long id){
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/category/{name}")
    public CategoryDTO getCategoryByName(@PathVariable(value = "name") String name){
        return categoryService.getCategoryByName(name);
    }

    @PutMapping("/category/{id}")
    public CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDetails, @PathVariable(value = "id") Long id){
        return categoryService.updateCategory(categoryDetails, id);
    }

    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @DeleteMapping("/category/{id}")
    public void deleteCategory(@PathVariable(value = "id") Long id){
        categoryService.deleteCategory(id);
    }
}
