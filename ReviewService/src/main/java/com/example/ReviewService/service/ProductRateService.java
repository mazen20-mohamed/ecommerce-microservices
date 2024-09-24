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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1/rate")
@RequiredArgsConstructor
@Slf4j
public class ProductRateService {
    private final ProductRateRepository productRateRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final RestTemplateService restTemplateService;

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

    public void addReviewForProduct(ProductRateRequest productRateRequest){
        Optional<Review> review = reviewRepository.findReview(productRateRequest.getProduct_id(),productRateRequest.getUser_id());

        if(review.isPresent()){
            throw new BadRequestException("Review is already exists");
        }

        restTemplateService.checkProduct(productRateRequest.getProduct_id());
        restTemplateService.checkUser(productRateRequest.getUser_id());


        ReviewId reviewId = new ReviewId(productRateRequest.getProduct_id(),
                productRateRequest.getUser_id());
        Review review1 = modelMapper.map(productRateRequest,Review.class);
        review1.setReviewId(reviewId);
        reviewRepository.save(review1);

        Optional<ProductRate> productRate = productRateRepository.findById(productRateRequest.getProduct_id());

        if(productRate.isEmpty()){
            log.info("product rate empty...");
            ProductRate productRate1 =
                    ProductRate.builder()
                            .rate(productRateRequest.getRate())
                            .numberOfRates(1)
                            .product_id(productRateRequest.getProduct_id())
                            .build();
            productRateRepository.save(productRate1);
            return;
        }
        log.info("Product found");
        ProductRate productRate1 = productRate.get();
        double sumOfRate = productRate1.getRate()*productRate1.getNumberOfRates();
        sumOfRate+=productRateRequest.getRate();
        double newRate = (sumOfRate/(productRate1.getNumberOfRates()+1));
        productRate1.setRate(newRate);
        productRate1.setProduct_id(productRate1.getProduct_id());
        productRate1.setNumberOfRates(productRate1.getNumberOfRates()+1);
        productRateRepository.save(productRate1);
    }


    public ReviewDto getReviewOfUser(String productId,String userId){
        Review review = reviewRepository.findReview(productId,userId)
                .orElseThrow(()->new NotFoundException("Not found review"));
        return ReviewDto.builder()
                .rate(review.getRate())
                .user_id(userId)
                .comment(review.getComment())
                .build();
    }

    public List<ReviewDto> getAllReviewOfProduct(String productId){
         Optional<List<Review>> reviews = reviewRepository.findReviewsOfProduct(productId);
        return reviews.map(reviewList -> reviewList.stream().map(review -> ReviewDto.builder()
                .user_id(review.getReviewId().getUser_id())
                .rate(review.getRate())
                .comment(review.getComment())
                .build()).toList()).orElseGet(List::of);
    }
}
