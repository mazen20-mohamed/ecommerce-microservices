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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public void createProduct(@Valid @ModelAttribute ProductRequest productRequest) throws IOException {
        productService.createProduct(productRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
    }


    @PutMapping("/{id}")
    public void updateProduct(@Valid @ModelAttribute ProductUpdate productRequest,
                              @PathVariable String id){
        productService.updateProduct(id,productRequest);
    }

    @GetMapping
    public List<ProductResponse> getProductsByIds(@RequestParam List<String> ids){
        return productService.getProductsByIds(ids);
    }

    @GetMapping("/{product_id}/exists")
    public boolean isProductExist(@PathVariable String product_id){
        return productService.isProductExist(product_id);
    }

    @GetMapping("/exists")
    public ResponseEntity<List<String>> isProductsExists(@RequestParam List<String> ids){
        return ResponseEntity.ok(productService.isProductsExists(ids));
    }

    @GetMapping("/price")
    public double getPriceOfAllProducts(@RequestParam List<String> ids){
        return productService.getPriceOfAllProducts(ids);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsResponse> getProductDetailsById(@PathVariable String id){
        return ResponseEntity.ok(productService.getProductDetailsById(id));
    }

    @GetMapping("/{page}/{size}")
    public PagedResponse<ProductResponse> getProductByCategory(@RequestParam ProductCategory category
            , @PathVariable int page, @PathVariable int size){
        return productService.getAllProductByCategory(category,page,size);
    }

    @GetMapping("/random/{page}/{size}")
    public PagedResponse<ProductResponse> getAllProductsRandom(@PathVariable int page ,@PathVariable int size){
        return productService.getAllProductsRandom(page,size);
    }

}

