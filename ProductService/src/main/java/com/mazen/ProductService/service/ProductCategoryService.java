package com.mazen.ProductService.service;

import com.mazen.ProductService.dto.CategoryDTO;
import com.mazen.ProductService.exceptions.NotFoundException;
import com.mazen.ProductService.model.ProductCategory;
import com.mazen.ProductService.repository.ProductCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public void createProductCategory(CategoryDTO categoryRequest){
        ProductCategory productCategory = ProductCategory.builder()
                .category(categoryRequest.category()).build();
        productCategoryRepository.save(productCategory);
    }

    private ProductCategory findProductCategory(int id){
        return productCategoryRepository.findById(id).orElseThrow(()->
                new NotFoundException("No category with this id"));
    }

    public String getCategoryById(int id){
        return findProductCategory(id).getCategory();
    }

    public List<String> getAllCategories(){
        return productCategoryRepository.findAll().stream().map(ProductCategory::getCategory).toList();
    }

    public void deleteCategory(int id){
        productCategoryRepository.delete(findProductCategory(id));
    }
}
