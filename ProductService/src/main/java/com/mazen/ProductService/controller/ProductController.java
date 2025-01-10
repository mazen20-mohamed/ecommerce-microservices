package com.mazen.ProductService.controller;
import com.mazen.ProductService.dto.ProductDetailsResponse;
import com.mazen.ProductService.dto.ProductResponse;
import com.mazen.ProductService.dto.request.post.ProductRequest;
import com.mazen.ProductService.dto.request.update.ProductUpdate;
import com.mazen.ProductService.model.ProductCategory;
import com.mazen.ProductService.service.ProductService;
import com.mazen.ProductService.util.PagedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void createProduct(@Valid @ModelAttribute ProductRequest productRequest,
                              @RequestHeader("Authorization") String authorization){
        productService.createProduct(productRequest,authorization);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateProduct(@Valid @ModelAttribute ProductUpdate productRequest,
                              @PathVariable String id,@RequestHeader("Authorization") String authorization){
        productService.updateProduct(id,productRequest,authorization);
    }

    @GetMapping
    public List<ProductResponse> getProductsByIds(@RequestParam List<String> ids,@RequestHeader("Authorization") String authorization) {
        return productService.getProductsByIds(ids,authorization);
    }

    @GetMapping("/{id}")
    public ProductDetailsResponse getProductDetailsById(@PathVariable String id,@RequestHeader("Authorization")String authorization){
        return productService.getProductDetailsById(id,authorization);
    }

    @GetMapping("/{page}/{size}")
    public PagedResponse<ProductResponse> getProductByCategory(@RequestParam ProductCategory category
            , @PathVariable int page, @PathVariable int size,@RequestHeader("Authorization") String authorization){
        return productService.getAllProductByCategory(category,page,size,authorization);
    }

    @GetMapping("all/{page}/{size}")
    public PagedResponse<ProductResponse> getAllProduct(@PathVariable int page, @PathVariable int size,@RequestHeader("Authorization") String authorization){
        return productService.getAllProduct(page,size,authorization);
    }

    @GetMapping("/random/{page}/{size}")
    public PagedResponse<ProductResponse> getAllProductsRandom(@PathVariable int page ,
                                                               @PathVariable int size,@RequestHeader("Authorization") String authorization){
        return productService.getAllProductsRandom(page,size,authorization);
    }

}
