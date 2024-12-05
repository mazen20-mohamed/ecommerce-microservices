package com.mazen.Cart.And.WishList.Service.controller;

import com.mazen.Cart.And.WishList.Service.dto.WishlistResponse;
import com.mazen.Cart.And.WishList.Service.security.extractUserId.ExtractUserId;
import com.mazen.Cart.And.WishList.Service.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/wishlist")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createWishList(@RequestParam String productId
            ,@RequestHeader("Authorization") String authorization,@ExtractUserId String userId) {
        wishListService.createWishList(productId,authorization,userId);
    }

    @GetMapping
    public List<WishlistResponse> getWishListProducts(@ExtractUserId String userId
            , @RequestHeader("Authorization") String authorization){
        return wishListService.getWishListProducts(userId,authorization);
    }

    @DeleteMapping("/{wishlistId}")
    public void deleteWishListProduct(@PathVariable long wishlistId){
        wishListService.deleteWishListProduct(wishlistId);
    }

    @DeleteMapping("/all")
    public void deleteWishLists(@ExtractUserId String userId){
        wishListService.deleteWishLists(userId);
    }

    @PutMapping("/{wishlistId}")
    public void moveToCart(@PathVariable long wishlistId){
        wishListService.moveToCart(wishlistId);
    }

}
