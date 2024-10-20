package com.mazen.Cart.And.WishList.Service.repository;

import com.mazen.Cart.And.WishList.Service.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WishListRepository extends JpaRepository<WishList,Long> {
    @Query("Select w from WishList w Where w.product_id = :productId And w.user_id = :userId")
    Optional<WishList> findByProductIdAndUserId(String productId,String userId);

    @Query("Select w from WishList w Where w.user_id = :userId")
    List<WishList> findByUserId(String userId);
}
