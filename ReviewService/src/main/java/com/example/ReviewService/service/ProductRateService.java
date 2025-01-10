package com.example.ReviewService.service;


import com.example.ReviewService.dto.ProductRateDto;
import com.example.ReviewService.dto.ProductRateRequest;
import com.example.ReviewService.dto.ReviewDto;
import com.example.ReviewService.exceptions.BadRequestException;
import com.example.ReviewService.exceptions.NotFoundException;
import com.example.ReviewService.model.ProductRate;
import com.example.ReviewService.model.Review;
import com.example.ReviewService.model.ReviewId;
import com.example.ReviewService.repository.ProductRateRepository;
import com.example.ReviewService.repository.ReviewRepository;
import com.example.ReviewService.service.feign.ProductClient;
import com.example.ReviewService.service.feign.AuthServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRateService implements IProductRateService{
    private final ProductRateRepository productRateRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final AuthServer userClient;
    private final ProductClient productClient;

    public ProductRateDto getProductRate(String productId){
        Optional<ProductRate> productRate = productRateRepository.findById(productId);
        if(productRate.isEmpty()){
            return ProductRateDto.builder()
                    .rate(5)
                    .numberOfRating(0)
                    .build();
        }
        return ProductRateDto.builder()
                .rate(productRate.get().getRate())
                .numberOfRating(productRate.get().getNumberOfRates())
                .build();
    }

    @Override
    public void addReviewForProduct(ProductRateRequest productRateRequest){
        Optional<Review> review = reviewRepository.findReview(productRateRequest.getProduct_id(),productRateRequest.getUser_id());

        if(review.isPresent()){
            throw new BadRequestException("Review is already exists");
        }

        productClient.getProductById(productRateRequest.getProduct_id());
        userClient.getUserData(productRateRequest.getUser_id());


        ReviewId reviewId = new ReviewId(productRateRequest.getProduct_id(),
                productRateRequest.getUser_id());
        Review review1 = modelMapper.map(productRateRequest,Review.class);
        review1.setReviewId(reviewId);
        reviewRepository.save(review1);

        Optional<ProductRate> productRate = productRateRepository.findById(productRateRequest.getProduct_id());

        if(productRate.isEmpty()){
            log.info("product rate empty {} ...",productRateRequest.getProduct_id());
            ProductRate productRate1 =
                    ProductRate.builder()
                            .rate(productRateRequest.getRate())
                            .numberOfRates(1)
                            .productId(productRateRequest.getProduct_id())
                            .build();
            productRateRepository.save(productRate1);
            return;
        }
        ProductRate productRate1 = productRate.get();
        double sumOfRate = productRate1.getRate()*productRate1.getNumberOfRates();
        sumOfRate+=productRateRequest.getRate();
        double newRate = (sumOfRate/(productRate1.getNumberOfRates()+1));
        productRate1.setRate(newRate);
        productRate1.setProductId(productRate1.getProductId());
        productRate1.setNumberOfRates(productRate1.getNumberOfRates()+1);
        productRateRepository.save(productRate1);
        log.info("Product with id {} has updated the rate",productRateRequest.getProduct_id());
    }


    @Override
    public ReviewDto getReviewOfUser(String productId,String userId){
        Review review = reviewRepository.findReview(productId,userId)
                .orElseThrow(()->new NotFoundException("Not found review"));
        return ReviewDto.builder()
                .rate(review.getRate())
                .user_id(userId)
                .comment(review.getComment())
                .build();
    }


    @Override
    public List<ReviewDto> getAllReviewOfProduct(String productId){
         Optional<List<Review>> reviews = reviewRepository.findReviewsOfProduct(productId);
        return reviews.map(reviewList -> reviewList.stream().map(review -> ReviewDto.builder()
                .user_id(review.getReviewId().getUser_id())
                .rate(review.getRate())
                .comment(review.getComment())
                .build()).toList()).orElseGet(List::of);
    }

}
