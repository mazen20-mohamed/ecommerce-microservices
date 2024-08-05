package com.mazen.ProductService.service;

import com.mazen.ProductService.dto.ProductDetailsResponse;
import com.mazen.ProductService.dto.request.ProductRequest;
import com.mazen.ProductService.dto.ProductResponse;
import com.mazen.ProductService.exceptions.NotFoundException;
import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductCategory;
import com.mazen.ProductService.model.ProductColor;
import com.mazen.ProductService.repository.ProductColorRepository;
import com.mazen.ProductService.repository.ProductRepository;
import com.mazen.ProductService.util.Colors;
import com.mazen.ProductService.util.PagedResponse;
import com.mazen.ProductService.util.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final MappingService mappingService;
    private final ProductColorRepository productColorRepository;
    private final RestTemplateService restTemplateService;
    private final ModelMapper modelMapper;


    @Transactional
    public void createProduct(ProductRequest productRequest) throws IOException {
        Product product = modelMapper.map(productRequest,Product.class);

        Product savedProduct = productRepository.save(product);
        List<ProductColor> productColors = productRequest
                .getProductColorRequests()
                .stream()
                .map(productColorRequest -> mappingService
                        .convertToProductColor(productColorRequest, savedProduct))
                .collect(Collectors.toList());

        savedProduct.setProductColors(productColors);
        List<MultipartFile> files = productRequest.getImages();
        List<String> imagesPaths = restTemplateService.saveImagesRequest(files, savedProduct);
        savedProduct.setImagesPaths(imagesPaths);

        productRepository.save(savedProduct);
    }

    @Transactional
    public void deleteProduct(String id){
        restTemplateService.deleteProductImages(id);
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

        product.setImagesPaths(restTemplateService.saveImagesRequest(productRequest.getImages(),product));
        List<ProductColor> productColors = productRequest
                .getProductColorRequests()
                .stream()
                .map(productColorRequest -> mappingService
                        .convertToProductColor(productColorRequest, product))
                .collect(Collectors.toList());

        product.setProductColors(productColors);

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
        return products.stream().map(mappingService::createProductResponse).toList();
    }

    @Transactional
    public void changeItemInventory(String productId, Size size, Colors color, int numberOfItems){
        Product product = getProductByIdOptional(productId);
        Optional<ProductColor> productColor =
                productColorRepository.findByProductSizeAndColor(size,color,product);
        if(productColor.isPresent()){
            productColor.get().setNumberInStock(numberOfItems);
            productColorRepository.save(productColor.get());
            return;
        }
        Optional<ProductColor> productColor1 =
                productColorRepository.findByProductByColor(color,product);
        if(productColor1.isPresent()){
            productColor1.get().setSize(size);
            productColor1.get().setColors(color);
            productColorRepository.save(productColor1.get());
            return;
        }
        ProductColor productColor2 = ProductColor.builder()
                .colors(color)
                .size(size)
                .product(product)
                .numberInStock(numberOfItems)
                .build();

        product.getProductColors().add(productColor2);
        productRepository.save(product);
    }

    @Transactional
    public void changeItemInventoryOnlyColor(String productId, Colors color, int numberOfItems){
        Product product = getProductByIdOptional(productId);

        Optional<ProductColor> productColor1 =
                productColorRepository.findByProductByColor(color,product);
        if(productColor1.isPresent()){
            productColor1.get().setColors(color);
            productColorRepository.save(productColor1.get());
            return;
        }
        ProductColor productColor2 = ProductColor.builder()
                .colors(color)
                .product(product)
                .numberInStock(numberOfItems)
                .build();

        product.getProductColors().add(productColor2);
        productRepository.save(product);
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

    public ProductDetailsResponse getProductDetailsById(String id){
        Product product = getProductByIdOptional(id);
        return mappingService.createProductDetailsResponse(product);
    }


    public PagedResponse<ProductResponse> getAllProductByCategory(ProductCategory category, int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Product> products= productRepository.getAllProductsByCategory(pageable, category);
        List<ProductResponse> productResponses = products
                .map(mappingService::createProductResponse).toList();
        return new PagedResponse<>(productResponses,
                page,size,products.getTotalElements(),
                products.getTotalPages(),products.isLast());
    }

    public PagedResponse<ProductResponse> getAllProductsRandom(int page , int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Product> products= productRepository.getAllProductsRandom(pageable);
        List<ProductResponse> productResponses = products
                .map(mappingService::createProductResponse).toList();
        return new PagedResponse<>(productResponses,
                page,size,products.getTotalElements(),
                products.getTotalPages(),products.isLast());
    }

}
