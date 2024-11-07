package com.mazen.Cart.And.WishList.Service.controller;

import com.mazen.Cart.And.WishList.Service.dto.ProductResponse;
import com.mazen.Cart.And.WishList.Service.dto.WishListRequest;
import com.mazen.Cart.And.WishList.Service.dto.WishlistResponse;
import com.mazen.Cart.And.WishList.Service.service.WishListService;
import jakarta.ws.rs.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/wishlist")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN') OR principal == #wishListRequest.user_id")
    public void createWishList(@RequestBody WishListRequest wishListRequest
            ,@RequestHeader("Authorization") String authorization) {
        wishListService.createWishList(wishListRequest,authorization);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') OR principal == #userId")
    public List<WishlistResponse> getWishListProducts(@PathVariable String userId
            , @RequestHeader("Authorization") String authorization){
        return wishListService.getWishListProducts(userId,authorization);
    }

    @DeleteMapping("/{wishlistId}")
    public void deleteWishListProduct(@PathVariable long wishlistId){
        wishListService.deleteWishListProduct(wishlistId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR principal == #wishListRequest.user_id")
    public void updateWishList(@RequestBody WishListRequest wishListRequest,@PathVariable long id){
        wishListService.updateWishList(wishListRequest,id);
    }


    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') OR principal == #userId")
    public void deleteWishLists(@PathVariable String userId){
        wishListService.deleteWishLists(userId);
    }


    @PutMapping("/{wishlistId}")
    @PreAuthorize("hasRole('ADMIN') OR principal == #userId")
    public void moveToCart(@PathVariable long wishlistId){
        wishListService.moveToCart(wishlistId);
    }

}
