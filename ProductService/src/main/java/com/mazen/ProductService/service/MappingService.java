package com.mazen.ProductService.service;

import com.mazen.ProductService.dto.*;
import com.mazen.ProductService.dto.request.post.ProductImageRequest;
import com.mazen.ProductService.dto.request.post.ProductSpecsRequest;
import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductImage;
import com.mazen.ProductService.model.ProductSpecs;
import com.mazen.ProductService.repository.ProductImageRepository;
import com.mazen.ProductService.repository.ProductSpecsRepository;
import com.mazen.ProductService.service.feignClient.FileServiceClient;
import com.mazen.ProductService.service.feignClient.SaleServiceClient;
import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MappingService {
    private final ModelMapper modelMapper;
    private final ProductImageRepository productImageRepository;
    private final ProductSpecsRepository productSpecsRepository;
    private final SaleServiceClient saleServiceClient;
    private final FileServiceClient fileServiceClient;


    // calculate price after discount
    public double getPriceAfterDiscount(String productId , double price){
        int discount =0;
        try{
            discount = saleServiceClient.getProductDiscountById(productId);
        }
        catch (FeignException ex){
            log.error(ex.getLocalizedMessage());
        }
        return (price*discount)/100.0;
    }


    public ProductSpecs convertToProductSpecs(ProductSpecsRequest productSpecsRequest,
                                              Product product){
        ProductSpecs productSpecs =modelMapper.map(productSpecsRequest,
                ProductSpecs.class);
        productSpecs.setProduct(product);
        return productSpecs;
    }

    public void convertToProductImage(ProductImageRequest productImageRequest,
                                              Product product,String authorization) throws IOException {
        ProductImage productImage = modelMapper.
                map(productImageRequest,ProductImage.class);

        productImage.setProduct(product);

        List<String> images = new ArrayList<>();

        try{
            images =  fileServiceClient.addPhotosToProduct(
                    productImageRequest.getImages(),
                    product.getId(),
                    productImageRequest.getColors(),
                    authorization);
        }
        catch (FeignException ex){
            if(ex.status()==500){
                log.error("Error with ");
            }
        }

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

    public ProductDetailsResponse createProductDetailsResponse(Product product){
        ProductDetailsResponse productDetailsResponse =
                modelMapper.map(product,ProductDetailsResponse.class);

        productDetailsResponse.setProductCategory(product.getProductCategory().getCategory());

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
        productDetailsResponse.setPriceAfterDiscount(getPriceAfterDiscount(product.getId(),product.getPrice()));
        productDetailsResponse.setProductSpecsResponses(productSpecsResponses);
        return productDetailsResponse;
    }


    // create product response
    public ProductResponse createProductResponse(Product product){

        ProductResponse productResponse = modelMapper.map(product,ProductResponse.class);

        productResponse.setProductCategory(product.getProductCategory().getCategory());

        String image = "";
        for( ProductImage productImage : product.getProductImages())
        {
            if(!productImage.getImagesPaths().isEmpty()){
                image = productImage.getImagesPaths().get(0);
            }
        }
        productResponse.setImageUrl(image);
        productResponse.setPriceAfterDiscount(getPriceAfterDiscount(product.getId(),product.getPrice()));
        return productResponse;
    }
}