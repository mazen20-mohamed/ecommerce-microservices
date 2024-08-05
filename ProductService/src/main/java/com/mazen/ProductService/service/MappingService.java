package com.mazen.ProductService.service;

import com.mazen.ProductService.dto.ProductColorResponse;
import com.mazen.ProductService.dto.ProductDetailsResponse;
import com.mazen.ProductService.dto.ProductResponse;
import com.mazen.ProductService.dto.request.ProductColorRequest;
import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductColor;
import com.mazen.ProductService.util.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
@Slf4j
public class MappingService {
    private final ModelMapper modelMapper;
    public ProductColor convertToProductColor(ProductColorRequest productColorRequest, Product product){
        ProductColor productColor1 =modelMapper.map(productColorRequest,ProductColor.class);
        productColor1.setProduct(product);
        return productColor1;
    }

    public ProductColorResponse createProductColorResponse(ProductColor productColor){
        return modelMapper.map(productColor,ProductColorResponse.class);
    }

    public ProductDetailsResponse createProductDetailsResponse(Product product){
        ProductDetailsResponse productDetailsResponse = modelMapper.map(product,ProductDetailsResponse.class);
        productDetailsResponse.setProductColorResponses(product.getProductColors()
                .stream().map(this::createProductColorResponse).toList());
        productDetailsResponse.setImagesUrl(product.getImagesPaths());
        return productDetailsResponse;
    }

    public ProductResponse createProductResponse(Product product){
        ProductResponse productResponse = modelMapper.map(product,ProductResponse.class);
        if(!product.getImagesPaths().isEmpty())
            productResponse.setImageUrl(product.getImagesPaths().get(0));
        return productResponse;
    }

}
