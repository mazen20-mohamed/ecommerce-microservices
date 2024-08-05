package com.example.SaleService.service;


import com.example.SaleService.exceptions.NotFoundException;
import com.example.SaleService.model.ProductSale;
import com.example.SaleService.repository.ProductSaleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSaleService {
    private final ProductSaleRepository productSaleRepository;

    public int getProductDiscountById(String productId){
        Optional<ProductSale> productSale =  productSaleRepository.findById(productId);
        return productSale.map(ProductSale::getDiscountPercent).orElse(0);
    }

    public List<Integer> getProductsDiscountByIds(List<String> ids){
        return ids.stream()
                .map(this::getProductDiscountById).toList();
    }

    public void makeDiscountOfProduct(String productId,int discount){
        Optional<ProductSale> productSale =  productSaleRepository.findById(productId);

        if(productSale.isPresent()){
            productSale.get().setDiscountPercent(discount);
            productSaleRepository.save(productSale.get());
        }

        ProductSale productSale1 = ProductSale.builder()
                .product_id(productId)
                .discountPercent(discount)
                .build();
        productSaleRepository.save(productSale1);
    }

}
