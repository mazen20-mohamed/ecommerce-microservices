package com.example.SaleService.controller;

import com.example.SaleService.service.ProductSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/discount")
@RequiredArgsConstructor
public class ProductSaleController {
    private final ProductSaleService productSaleService;

    @PostMapping
    public void makeDiscountOfProduct(@RequestParam String productId,
                                      @RequestParam int discount){
        productSaleService.makeDiscountOfProduct(productId,discount);
    }

    @GetMapping("/{productId}")
    public int getProductDiscountById(@PathVariable String productId){
        return productSaleService.getProductDiscountById(productId);
    }

    @GetMapping("/products")
    public List<Integer> getProductsDiscountByIds(@RequestParam List<String> ids){
        return productSaleService.getProductsDiscountByIds(ids);
    }


}
