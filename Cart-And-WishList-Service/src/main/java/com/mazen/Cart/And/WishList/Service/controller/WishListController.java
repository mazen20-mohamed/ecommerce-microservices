package com.mazen.Cart.And.WishList.Service.controller;

import com.mazen.Cart.And.WishList.Service.dto.ProductResponse;
import com.mazen.Cart.And.WishList.Service.dto.WishListRequest;
import com.mazen.Cart.And.WishList.Service.service.WishListService;
import jakarta.ws.rs.HttpMethod;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/wishlist")
@RequiredArgsConstructor
public class WishListController {
    private final WishListService wishListService;

    @GetMapping("/{user_id}")
    public ResponseEntity<List<ProductResponse>> getWishListProducts(@PathVariable String user_id){
        return ResponseEntity.ok(wishListService.getWishListProducts(user_id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createWishList(@RequestBody WishListRequest wishListRequest){
        wishListService.createWishList(wishListRequest);
    }

    @DeleteMapping("/{productId}/{userId}")
    public void deleteWishListProduct(@PathVariable String productId,@PathVariable String userId){
        wishListService.deleteWishListProduct(productId,userId);
    }

    @PutMapping("/{id}")
    public void updateWishList(@RequestBody WishListRequest wishListRequest,@PathVariable long id){
        wishListService.updateWishList(wishListRequest,id);
    }

    @GetMapping("/{productId}/{userId}")
    public ResponseEntity<Boolean> isProductInTheWishList(String productId,String userId){
        return ResponseEntity.ok(wishListService.isProductInTheWishList(productId,userId));
    }

}
