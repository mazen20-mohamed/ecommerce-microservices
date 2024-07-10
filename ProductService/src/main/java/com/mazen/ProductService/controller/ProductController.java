package com.mazen.ProductService.controller;

import com.mazen.ProductService.dto.ProductRequest;
import com.mazen.ProductService.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/product")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public void createProduct(@Valid @ModelAttribute ProductRequest productRequest) throws IOException {
        productService.createProduct(productRequest);
    }


}
