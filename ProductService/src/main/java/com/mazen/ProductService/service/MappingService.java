package com.mazen.ProductService.service;


import com.mazen.ProductService.dto.*;
import com.mazen.ProductService.dto.request.post.ProductImageRequest;
import com.mazen.ProductService.dto.request.post.ProductSpecsRequest;
import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductImage;
import com.mazen.ProductService.model.ProductSpecs;
import com.mazen.ProductService.repository.ProductImageRepository;
import com.mazen.ProductService.repository.ProductSpecsRepository;
import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
@Slf4j
public class MappingService {
    private final ModelMapper modelMapper;
    private final RestTemplateService restTemplateService;
    private final ProductImageRepository productImageRepository;
    private final ProductSpecsRepository productSpecsRepository;


    public ProductSpecs convertToProductSpecs(ProductSpecsRequest productSpecsRequest,
                                              Product product){
        ProductSpecs productSpecs =modelMapper.map(productSpecsRequest,
                ProductSpecs.class);
        productSpecs.setProduct(product);
        return productSpecs;
    }

    public void convertToProductImage(ProductImageRequest productImageRequest,
                                              Product product) throws IOException {
        ProductImage productImage = modelMapper.
                map(productImageRequest,ProductImage.class);

        productImage.setProduct(product);

        List<String> images =  restTemplateService.saveImagesRequest(productImageRequest.getImages(),
                product,productImageRequest.getColors());

        productImage.setImagesPaths(images);
        productImageRepository.save(productImage);
    }

    public SpecsDetails createSpecsDetails(Size size , int numberOfItems){
        return new SpecsDetails(size,numberOfItems);
    }

    public ProductSpecsResponse createSpecsResponse(List<ProductSpecs> productSpecs){

        ProductSpecsResponse productSpecsResponse = ProductSpecsResponse.builder()
                .colors(productSpecs.get(0).getColors())
                .build();
        List<SpecsDetails> specsDetails = productSpecs.stream().map(productSpecs1 -> this.createSpecsDetails
                (productSpecs1.getSize(),productSpecs1.getNumberInStock())).toList();

        productSpecsResponse.setDetails(specsDetails);
        return productSpecsResponse;
    }

    public List<String> createProductImageResponse(ProductImage productImage){
        return productImage.getImagesPaths();
    }

    public ProductDetailsResponse createProductDetailsResponse(Product product){
        ProductDetailsResponse productDetailsResponse =
                modelMapper.map(product,ProductDetailsResponse.class);


        List<ProductSpecsResponse> productSpecsResponses = new ArrayList<>();

        Arrays.stream(Colors.values()).forEach(colors -> {
                    List<ProductSpecs> productSpecs = productSpecsRepository.findByColor(colors,product);
                    List<ProductImage> productImages = productImageRepository.findByColor(colors,product);
                    if(!productSpecs.isEmpty() && !productImages.isEmpty()){
                        ProductSpecsResponse productSpecsResponse = this.createSpecsResponse(productSpecs);
                        productSpecsResponse.setImagesUrl(productImages.get(0).getImagesPaths());
                        productSpecsResponses.add(productSpecsResponse);
                    }
        });

        productDetailsResponse.setProductSpecsResponses(productSpecsResponses);

        return productDetailsResponse;
    }

    public ProductResponse createProductResponse(Product product){

        ProductResponse productResponse = modelMapper.map(product,ProductResponse.class);
        String image = "";
        for( ProductImage productImage : product.getProductImages())
        {
            if(!productImage.getImagesPaths().isEmpty()){
                image = productImage.getImagesPaths().get(0);
            }
        }
        productResponse.setImageUrl(image);
        return productResponse;
    }
}