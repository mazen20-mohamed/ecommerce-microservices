package com.example.ReviewService.kafka;

import com.example.ReviewService.model.Review;
import com.example.ReviewService.repository.ProductRateRepository;
import com.example.ReviewService.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewProductConsumer {
    private final ProductRateRepository productRateRepository;
    private final ReviewRepository reviewRepository;

    @KafkaListener(topics = "product-deletion",groupId = "productGroup")
    public void consumeProductDeletion(String id) throws MessagingException {
        log.info("Consuming product deletion from product service with id :: {}",id);
        // delete reviews of product...
        Optional<List<Review>> reviews = reviewRepository.findReviewsOfProduct(id);
        reviews.ifPresent(reviewList -> reviewList.forEach(reviewRepository::delete));

        // delete rate of the product
        productRateRepository.deleteById(id);
    }
}
