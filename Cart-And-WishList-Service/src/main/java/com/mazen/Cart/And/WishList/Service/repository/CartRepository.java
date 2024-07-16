package com.mazen.Cart.And.WishList.Service.repository;

import com.mazen.Cart.And.WishList.Service.model.Cart;
import com.mazen.Cart.And.WishList.Service.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("Select c from Cart c Where c.product_id = :productId And c.user_id = :userId")
    Optional<Cart> findByProductIdAndUserId(String productId, String userId);

    @Query("Select c from Cart c Where c.user_id = :userId")
    Optional<List<Cart>> findByUserId(String userId);
}
