package com.mazen.ProductService.service;

import com.mazen.ProductService.dto.ProductColorRequest;
import com.mazen.ProductService.dto.ProductRequest;
import com.mazen.ProductService.dto.ProductResponse;
import com.mazen.ProductService.dto.ProductSizeRequest;
import com.mazen.ProductService.exceptions.NotFoundException;
import com.mazen.ProductService.exceptions.ServerErrorException;
import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductColor;
import com.mazen.ProductService.model.ProductSize;
import com.mazen.ProductService.repository.ProductColorRepository;
import com.mazen.ProductService.repository.ProductRepository;
import com.mazen.ProductService.repository.ProductSizeRepository;
import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.Size;
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

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;
    private final ProductSizeRepository productSizeRepository;
    private final ProductColorRepository productColorRepository;

    private ProductColor convertToProductColor(ProductColorRequest productColorRequest,ProductSize productSize){
        ProductColor productColor1 =modelMapper.map(productColorRequest,ProductColor.class);
        productColor1.setProductSize(productSize);
        return productColor1;
    }

    private ProductSize convertToProductSize(ProductSizeRequest productSizeRequest,Product product){
        ProductSize productSize = modelMapper.map(productSizeRequest,ProductSize.class);

        List<ProductColor> productColors = productSizeRequest.getProductColorRequests().
                        stream().map(productColorRequest -> this.convertToProductColor(productColorRequest,productSize)).toList();

        productSize.setProductColors(productColors);

        productSize.setProduct(product);
        return productSize;
    }

    public List<String> saveImagesRequest(List<MultipartFile> files,Product product1) throws IOException {
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
        if(!response.getStatusCode().equals(HttpStatus.OK)){
            log.error("Error with server when saving images");
            throw new ServerErrorException("Error with server when saving images");
        }
        return response.getBody();
    }


    @Transactional
    public void createProduct(ProductRequest productRequest) throws IOException {
        Product product = modelMapper.map(productRequest,Product.class);

        List<ProductSize> productSizes =  productRequest.getProductSizeRequests()
                .stream().map(productSizeRequest ->  this.convertToProductSize(productSizeRequest,product))
                .toList();

        product.setProductSizes(productSizes);
        Product product1 = productRepository.save(product);
        List<MultipartFile> files = productRequest.getImages();

        product1.setImagesPaths(saveImagesRequest(files,product1));
        productRepository.save(product1);
    }

    @Transactional
    public void deleteProduct(String id){
        String serverUrl = "http://localhost:8300/v1/file/"+id;
        ResponseEntity<Boolean> response = restTemplate.exchange(
                serverUrl,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Boolean>() {}
        );
        if(!response.getStatusCode().equals(HttpStatus.OK)){
            log.error("Error with server when deleting images");
            throw new ServerErrorException("Error with server when deleting images");
        }
        productRepository.deleteById(id);
    }

    public Product getProductByIdOptional(String id){
        return productRepository.findById(id).orElseThrow(()->
                new NotFoundException("Product not found with id "+id));
    }

    @Transactional
    public void updateProduct(String id,ProductRequest productRequest) throws IOException {
        Product product = getProductByIdOptional(id);
        modelMapper.map(productRequest,product);

        product.setImagesPaths(saveImagesRequest(productRequest.getImages(),product));
        List<ProductSize> productSizes =  productRequest.getProductSizeRequests()
                .stream().map(productSizeRequest ->  this.convertToProductSize(productSizeRequest,product))
                .toList();

        product.setProductSizes(productSizes);

        productRepository.save(product);
    }

    public List<ProductResponse> getProductsByIds(List<String> ids){
        List<Product> products = productRepository.findAllById(ids);
        // Extract IDs of found products
        List<String> foundIds = products.stream()
                .map(Product::getId)
                .toList();

        // Identify missing IDs
        List<String> missingIds = ids.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new NotFoundException("Products not found with ids: " + missingIds);
        }
        return products.stream().map(prod->{
            ProductResponse productResponse =  modelMapper.map(prod,ProductResponse.class);
            if(!prod.getImagesPaths().isEmpty())
                productResponse.setImageUrl(prod.getImagesPaths().get(0));
            return productResponse;
        }).toList();
    }

    @Transactional
    public void changeItemInventory(String productId, Size size, Colors color, int numberOfItems){
        Product product = getProductByIdOptional(productId);
        Optional<ProductSize> productSize =
                productSizeRepository.findByProductAndSize(product,size);
        if(productSize.isEmpty()){
            ProductSize productSize1 = ProductSize.builder()
                    .product(product)
                    .size(size).build();
            ProductColor productColor = ProductColor.builder()
                    .productSize(productSize1)
                    .colors(color)
                    .numberInStock(numberOfItems)
                    .build();
            productSize1.getProductColors().add(productColor);
            productSizeRepository.save(productSize1);
            return;
        }

        Optional<ProductColor> productColor =
                productColorRepository.findByProductSizeAndColor(productSize.get(),color);
        if(productColor.isEmpty()){
            ProductColor productColor1 = ProductColor.builder()
                    .productSize(productSize.get())
                    .colors(color)
                    .numberInStock(numberOfItems)
                    .build();
            productSize.get().getProductColors().add(productColor1);
            productSizeRepository.save(productSize.get());
            return;
        }


        productColor.get().setNumberInStock(numberOfItems);
        productColorRepository.save(productColor.get());
    }

    public boolean isProductExist(String product_id){
        Optional<Product> product = productRepository.findById(product_id);
        return product.isPresent();
    }

    public boolean isProductsExists(List<String> ids){
        List<String> idsNotFound = new ArrayList<>();
        log.info(ids.toString());
        ids.forEach(id->{
            if(!isProductExist(id)){
                idsNotFound.add(id);
            }
        });
        return idsNotFound.isEmpty();
    }

}
