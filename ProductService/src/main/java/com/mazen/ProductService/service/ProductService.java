package com.mazen.ProductService.service;
import com.mazen.ProductService.dto.OrderStatus;
import com.mazen.ProductService.dto.ProductDetailsResponse;
import com.mazen.ProductService.dto.ProductResponse;
import com.mazen.ProductService.dto.request.post.ProductRequest;
import com.mazen.ProductService.dto.request.update.ProductUpdate;
import com.mazen.ProductService.exceptions.BadRequestException;
import com.mazen.ProductService.exceptions.NotFoundException;
import com.mazen.ProductService.kafka.ProductProducer;
import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductCategory;
import com.mazen.ProductService.model.ProductImage;
import com.mazen.ProductService.model.ProductSpecs;
import com.mazen.ProductService.repository.ProductCategoryRepository;
import com.mazen.ProductService.repository.ProductSpecsRepository;
import com.mazen.ProductService.repository.ProductRepository;
import com.mazen.ProductService.service.feignClient.FileServiceClient;
import com.mazen.ProductService.service.feignClient.OrderServiceClient;
import com.mazen.ProductService.service.feignClient.SaleServiceClient;
import com.mazen.ProductService.util.PagedResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductSpecsRepository productSpecsRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ModelMapper modelMapper;
    private final MappingService mappingService;
    private final FileServiceClient fileServiceClient;
    private final ProductProducer productProducer;
    private final OrderServiceClient orderServiceClient;

    @Transactional
    public void createProduct(ProductRequest productRequest,String authorization) {

        log.info("Product request {}",productRequest.toString());
        // check if product category exists in DB
        ProductCategory productCategory = productCategoryRepository.findByCategory
                (productRequest.getProductCategory()).orElseThrow(()->
                new NotFoundException("Not found product category with the name"));

        // build product object
        Product product = Product.builder()
                .title(productRequest.getTitle())
                .price(productRequest.getPrice())
                .productCategory(productCategory)
                .build();

        // build specs list of product
        List<ProductSpecs> productSpecs = productRequest
                .getProductSpecsRequests()
                .stream()
                .map(productSpecsRequest -> mappingService
                        .convertToProductSpecs(productSpecsRequest,product))
                .toList();

        // set the values
        product.setProductSpecs(productSpecs);
        Product product1 = productRepository.save(product);

        // loop over images from request and sent to file storage service
        productRequest
                .getProductImageRequests().forEach(productImageRequest -> {
                    try {
                        mappingService.convertToProductImage(productImageRequest,product1,authorization);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Transactional
    public void deleteProduct(String id){
        List<Object> orders = orderServiceClient.getOrdersByProductId(id,List.of(OrderStatus.CURRENT
                ,OrderStatus.DELIVERED,OrderStatus.PACKING,OrderStatus.SHIPPED));
        if(!orders.isEmpty()) {
            throw new BadRequestException("There are some orders currently...");
        }
        productProducer.sendProductId(id);
        productRepository.deleteById(id);
    }

    public Product getProductByIdOptional(String id){
        return productRepository.findById(id).orElseThrow(()->
                new NotFoundException("Product not found with id "+id));
    }

    @Transactional
    public void updateProduct(String id, ProductUpdate productUpdateRequest,String authorization){

        Product product = getProductByIdOptional(id);

        modelMapper.map(product,productUpdateRequest);

        List<ProductSpecs> productSpecsList = product.getProductSpecs();

        List<ProductSpecs> productSpecs = productUpdateRequest.getProductSpecsUpdates().stream().map(
                productSpecsUpdate ->{
                    Optional<ProductSpecs> productSpecs1 =
                    productSpecsRepository.findByColorAndSize(productSpecsUpdate.getColors(),
                            product,productSpecsUpdate.getSize());
                    if(productSpecs1.isEmpty()){
                        return ProductSpecs.builder()
                                .product(product)
                                .size(productSpecsUpdate.getSize())
                                .numberInStock(productSpecsUpdate.getNumberInStock())
                                .colors(productSpecsUpdate.getColors())
                                .build();
                    }
                    productSpecsList.remove(productSpecs1.get());
                    productSpecs1.get().setNumberInStock(productSpecsUpdate.getNumberInStock());
                    return productSpecs1.get();
        }).toList();

        for(ProductSpecs productSpecs1 : productSpecsList){
            productSpecsRepository.delete(productSpecs1);
        }

        product.setProductSpecs(productSpecs);

        product.getProductImages().clear();

        List<ProductImage> productImages = productUpdateRequest.getProductImageUpdates().stream().map(
                productImageUpdate -> {
                    List<String> allImages = new ArrayList<>();
                    if((productImageUpdate.getImagesUrl()==null || productImageUpdate.getImagesUrl().isEmpty())
                    && (productImageUpdate.getImages() == null || productImageUpdate.getImages().isEmpty())){
                        throw new BadRequestException("There is no images with request");
                    }
                    if(!(productImageUpdate.getImagesUrl()==null || productImageUpdate.getImagesUrl().isEmpty())){
                        List<String> images = productImageUpdate.getImagesUrl();
                        allImages.addAll(images);
                    }

                    try {
                        if(!(productImageUpdate.getImages() == null || productImageUpdate.getImages().isEmpty())){
                            List<String> imageRequests = fileServiceClient.updatePhotos(
                                    productImageUpdate.getImages(),
                                    product.getId(),
                                    productImageUpdate.getColors(),
                                    authorization);

                            allImages.addAll(imageRequests);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return ProductImage.builder()
                            .colors(productImageUpdate.getColors())
                            .product(product)
                            .imagesPaths(allImages)
                            .build();
                }).toList();

        product.setProductImages(productImages);

        productRepository.save(product);
    }

    public List<ProductResponse> getProductsByIds(List<String> ids,String authorization){
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
        return products.stream().map(product->mappingService.createProductResponse(product,authorization)).toList();
    }

    public ProductDetailsResponse getProductDetailsById(String id,String authorization){
        Product product = getProductByIdOptional(id);
        return mappingService.createProductDetailsResponse(product,authorization);
    }

    public PagedResponse<ProductResponse> getAllProductByCategory(ProductCategory category,
                                                                  int page, int size,
                                                                  String authorization){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Product> products= productRepository.getAllProductsByCategory(pageable, category);
        List<ProductResponse> productResponses = products
                .map(product -> mappingService.createProductResponse(product,authorization)).toList();
        return new PagedResponse<>(productResponses,
                page,size,products.getTotalElements(),
                products.getTotalPages(),products.isLast());
    }

    public PagedResponse<ProductResponse> getAllProductsRandom(int page , int size,String authorization){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Product> products= productRepository.getAllProductsRandom(pageable);
        List<ProductResponse> productResponses = products
                .map(product -> mappingService.createProductResponse(product,authorization)).toList();
        return new PagedResponse<>(productResponses,
                page,size,products.getTotalElements(),
                products.getTotalPages(),products.isLast());
    }

    public PagedResponse<ProductResponse> getAllProduct(int page, int size,String authorization) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Product> products= productRepository.findAll(pageable);
        List<ProductResponse> productResponses = products
                .map(product -> mappingService.createProductResponse(product,authorization)).toList();
        return new PagedResponse<>(productResponses,
                page,size,products.getTotalElements(),
                products.getTotalPages(),products.isLast());
    }
}
