package com.mazen.ProductService.service;

import com.mazen.ProductService.dto.ProductColorRequest;
import com.mazen.ProductService.dto.ProductRequest;
import com.mazen.ProductService.dto.ProductSizeRequest;
import com.mazen.ProductService.exceptions.ServerErrorException;
import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductColor;
import com.mazen.ProductService.model.ProductSize;
import com.mazen.ProductService.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    private ProductColor convertToProductColor(ProductColorRequest productColorRequest,ProductSize productSize){
        ProductColor productColor = modelMapper.map(productColorRequest,ProductColor.class);
        productColor.setProductSize(productSize);
        return productColor;
    }
    private ProductSize convertToProductSize(ProductSizeRequest productSizeRequest,Product product){
        ProductSize productSize = modelMapper.map(productSizeRequest,ProductSize.class);
        productSize.setProduct(product);

        List<ProductColor> productColors = productSizeRequest.getProductColorRequests().
                        stream().map(prod->this.convertToProductColor(prod, productSize)).toList();
        log.info(productSize.toString());
        productSize.setProductColors(productColors);

        return productSize;
    }

    @Transactional
    public void createProduct(ProductRequest productRequest) throws IOException {
        Product product = modelMapper.map(productRequest,Product.class);

        List<ProductSize> productSizes =  productRequest.getProductSizeRequests()
                .stream().map(prod->this.convertToProductSize(prod, product))
                .toList();
        product.setProductSizes(productSizes);
        Product product1 = productRepository.save(product);
        List<MultipartFile> files = productRequest.getImages();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for(MultipartFile f : files){
            ByteArrayResource resource = new ByteArrayResource(f.getBytes()) {
                @Override
                public String getFilename() {
                    return f.getOriginalFilename();
                }
            };
            body.add("images", resource);
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String serverUrl = "http://localhost:8300/v1/file/"+product1.getId();

        ResponseEntity<List<String>> response = restTemplate.exchange(
                serverUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<String>>() {}
        );
        if(response.getStatusCode()== HttpStatus.OK){
            product1.setImagesPaths(response.getBody());
            productRepository.save(product1);
            return;
        }
        log.error("Error with server when saving images");
        throw new ServerErrorException("Error with server when saving images");
    }

    
}
