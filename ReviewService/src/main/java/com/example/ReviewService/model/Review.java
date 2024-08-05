package com.example.ReviewService.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_product_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @EmbeddedId
    private ReviewId reviewId;

    private double rate;

    private String comment;
}
