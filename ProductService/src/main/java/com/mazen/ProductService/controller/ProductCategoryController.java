package com.mazen.ProductService.controller;

import com.mazen.ProductService.dto.CategoryDTO;
import com.mazen.ProductService.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/productsCategory")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void createProductCategory(@RequestBody CategoryDTO categoryRequest){
        productCategoryService.createProductCategory(categoryRequest);
    }

    @GetMapping("/{id}")
    public String getCategoryById(@PathVariable int id){
        return productCategoryService.getCategoryById(id);
    }

    @GetMapping
    public List<String> getAllCategories(){
        return productCategoryService.getAllCategories();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCategory(@PathVariable int id){
        productCategoryService.deleteCategory(id);
    }

}
