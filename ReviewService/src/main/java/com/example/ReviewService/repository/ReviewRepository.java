package com.example.ReviewService.repository;

import com.example.ReviewService.model.Review;
import com.example.ReviewService.model.ReviewId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, ReviewId> {

    @Query("Select r from Review r Where r.reviewId.product_id = :product_id And r.reviewId.user_id = :user_id")
    Optional<Review> findReview(String product_id,String user_id);


    @Query("Select r from Review r Where r.reviewId.product_id = :product_id")
    Optional<List<Review>> findReviewsOfProduct(String product_id);
}
