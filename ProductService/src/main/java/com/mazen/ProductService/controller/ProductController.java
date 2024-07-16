package com.mazen.ProductService.controller;

import com.mazen.ProductService.dto.ProductRequest;
import com.mazen.ProductService.dto.ProductResponse;
import com.mazen.ProductService.service.ProductService;
import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import jakarta.validation.Valid;
import jakarta.ws.rs.Path;
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


    @GetMapping("/isProductsExists")
    public ResponseEntity<Boolean> isProductsExists(@RequestParam List<String> ids){
        return ResponseEntity.ok(productService.isProductsExists(ids));
    }


}
