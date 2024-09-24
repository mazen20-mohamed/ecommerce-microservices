package com.mazen.ProductService.service;
import com.mazen.ProductService.dto.ProductDetailsResponse;
import com.mazen.ProductService.dto.ProductResponse;
import com.mazen.ProductService.dto.request.post.ProductRequest;
import com.mazen.ProductService.dto.request.update.ProductSpecsUpdate;
import com.mazen.ProductService.dto.request.update.ProductUpdate;
import com.mazen.ProductService.exceptions.BadRequestException;
import com.mazen.ProductService.exceptions.NotFoundException;
import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.model.ProductCategory;
import com.mazen.ProductService.model.ProductImage;
import com.mazen.ProductService.model.ProductSpecs;
import com.mazen.ProductService.repository.ProductImageRepository;
import com.mazen.ProductService.repository.ProductSpecsRepository;
import com.mazen.ProductService.repository.ProductRepository;
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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final MappingService mappingService;
    private final ProductSpecsRepository productSpecsRepository;
    private final RestTemplateService restTemplateService;
    private final ModelMapper modelMapper;
    private final SaleServiceClient saleServiceClient;


    @Transactional
    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .title(productRequest.getTitle())
                .price(productRequest.getPrice())
                .productCategory(productRequest.getProductCategory())
                .build();

        List<ProductSpecs> productSpecs = productRequest
                .getProductSpecsRequests()
                .stream()
                .map(productSpecsRequest -> mappingService
                        .convertToProductSpecs(productSpecsRequest,product))
                .toList();
        product.setProductSpecs(productSpecs);

        Product product1 = productRepository.save(product);

        productRequest
                .getProductImageRequests().forEach(productImageRequest -> {
                    try {
                        mappingService.convertToProductImage(productImageRequest,product1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

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
    public void updateProduct(String id, ProductUpdate productUpdateRequest){

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
                            List<String> imageRequests = restTemplateService.updateImagesRequest(
                                    productImageUpdate.getImages(),product,productImageUpdate.getColors());
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

//    @Transactional
//    public void changeItemInventory(String productId, Size size, Colors color, int numberOfItems){
//        Product product = getProductByIdOptional(productId);
//        Optional<ProductColor> productColor =
//                productColorRepository.findByProductSizeAndColor(size,color,product);
//        if(productColor.isPresent()){
//            productColor.get().setNumberInStock(numberOfItems);
//            productColorRepository.save(productColor.get());
//            return;
//        }
//        Optional<ProductColor> productColor1 =
//                productColorRepository.findByProductByColor(color,product);
//        if(productColor1.isPresent()){
//            productColor1.get().setSize(size);
//            productColor1.get().setColors(color);
//            productColorRepository.save(productColor1.get());
//            return;
//        }
//        ProductColor productColor2 = ProductColor.builder()
//                .colors(color)
//                .size(size)
//                .product(product)
//                .numberInStock(numberOfItems)
//                .build();
//
//        product.getProductColors().add(productColor2);
//        productRepository.save(product);
//    }
//
//    @Transactional
//    public void changeItemInventoryOnlyColor(String productId, Colors color, int numberOfItems){
//        Product product = getProductByIdOptional(productId);
//
//        Optional<ProductColor> productColor1 =
//                productColorRepository.findByProductByColor(color,product);
//        if(productColor1.isPresent()){
//            productColor1.get().setColors(color);
//            productColorRepository.save(productColor1.get());
//            return;
//        }
//        ProductColor productColor2 = ProductColor.builder()
//                .colors(color)
//                .product(product)
//                .numberInStock(numberOfItems)
//                .build();
//
//        product.getProductColors().add(productColor2);
//        productRepository.save(product);
//    }
    public boolean isProductExist(String product_id){
        Optional<Product> product = productRepository.findById(product_id);
        return product.isPresent();
    }

    public List<String> isProductsExists(List<String> ids){
        List<String> idsNotFound = new ArrayList<>();
        log.info(ids.toString());
        ids.forEach(id->{
            if(!isProductExist(id)){
                idsNotFound.add(id);
            }
        });
        return idsNotFound;
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

    public double getPriceOfAllProducts(List<String> ids){
        double sum = 0.0;
        List<Integer> discounts = new ArrayList<>();

        try{
             discounts = saleServiceClient.getProductsDiscountByIds(ids);
        }
        catch (FeignException ex){
            log.error(ex.getLocalizedMessage());
        }

        int index = 0;
        for(String id : ids){
            Product product = getProductByIdOptional(id);
            int discount = discounts.get(index);
            double price =  (product.getPrice()*discount)/100.0;
            sum+= price;
            index++;
        }
         return sum;
    }

}
