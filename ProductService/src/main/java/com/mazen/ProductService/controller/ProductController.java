package com.mazen.ProductService.controller;

import com.mazen.ProductService.dto.ProductDetailsResponse;
import com.mazen.ProductService.dto.request.ProductRequest;
import com.mazen.ProductService.dto.ProductResponse;
import com.mazen.ProductService.model.ProductCategory;
import com.mazen.ProductService.service.ProductService;
import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.PagedResponse;
import com.mazen.ProductService.util.Size;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/product")
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

    @PutMapping("{id}")
    public void updateProduct(@Valid @ModelAttribute ProductRequest productRequest,@PathVariable String id) throws IOException {
        productService.updateProduct(id,productRequest);
    }

    @GetMapping("/ids")
    public List<ProductResponse> getProductsByIds(@RequestParam List<String> ids){
        return productService.getProductsByIds(ids);
    }

    @PatchMapping("/{productId}")
    public void changeNumberOfItemInventory(String productId,@RequestParam Size size,@RequestParam Colors color,@RequestParam int numberOfItems){
        productService.changeItemInventory(productId,size,color,numberOfItems);
    }

    @PatchMapping("/{productId}/{color}")
    public void changeItemInventoryOnlyColor(@PathVariable String productId,@PathVariable Colors color,@RequestParam int numberOfItems){
        productService.changeItemInventoryOnlyColor(productId,color,numberOfItems);
    }

    @GetMapping("/check/{product_id}")
    public boolean isProductExist(@PathVariable String product_id){
        return productService.isProductExist(product_id);
    }

    @GetMapping("/isProductsExists")
    public ResponseEntity<Boolean> isProductsExists(@RequestParam List<String> ids){
        return ResponseEntity.ok(productService.isProductsExists(ids));
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ProductDetailsResponse> getProductDetailsById(@PathVariable String id){
        return ResponseEntity.ok(productService.getProductDetailsById(id));
    }

    @GetMapping("/{page}/{size}")
    public PagedResponse<ProductResponse> getAllProductByCategory(@RequestParam ProductCategory category
            ,@PathVariable int page,@PathVariable int size){
        return productService.getAllProductByCategory(category,page,size);
    }

    @GetMapping("/random/{page}/{size}")
    public PagedResponse<ProductResponse> getAllProductsRandom(@PathVariable int page ,@PathVariable int size){
        return productService.getAllProductsRandom(page,size);
    }
}
