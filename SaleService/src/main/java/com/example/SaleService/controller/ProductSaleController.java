package com.example.SaleService.controller;

import com.example.SaleService.service.ProductSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/productSale")
@RequiredArgsConstructor
public class ProductSaleController {
    private final ProductSaleService productSaleService;

    @GetMapping("/{productId}")
    public int getProductDiscountById(@PathVariable String productId){
        return productSaleService.getProductDiscountById(productId);
    }

    @GetMapping("/all")
    public List<Integer> getProductsDiscountByIds(@RequestParam List<String> ids){
        return productSaleService.getProductsDiscountByIds(ids);
    }

    @PostMapping("/{productId}")
    public void makeDiscountOfProduct(@PathVariable String productId,
                                      @RequestParam int discount){
        productSaleService.makeDiscountOfProduct(productId,discount);
    }

}
