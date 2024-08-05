package com.example.ReviewService.model;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReviewId implements Serializable {
    private String product_id;
    private String user_id;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewId reviewId)) return false;
        return Objects.equals(product_id, reviewId.product_id) &&
                Objects.equals(user_id, reviewId.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product_id, user_id);
    }
}
