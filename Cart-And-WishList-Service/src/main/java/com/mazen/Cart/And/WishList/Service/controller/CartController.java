package com.mazen.Cart.And.WishList.Service.controller;
import com.mazen.Cart.And.WishList.Service.dto.CartRequest;
import com.mazen.Cart.And.WishList.Service.dto.CartResponse;
import com.mazen.Cart.And.WishList.Service.security.extractUserId.ExtractUserId;
import com.mazen.Cart.And.WishList.Service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCartItem(@RequestBody CartRequest cartRequest,@ExtractUserId String userId){
        cartService.createCartItem(cartRequest,userId);
    }

    @DeleteMapping("/{cartId}")
    public void deleteCartItem(@PathVariable long cartId){
        cartService.deleteCartItem(cartId);
    }

    @DeleteMapping("/all")
    public void deleteAllCartItems(@ExtractUserId String userId){
        cartService.deleteAllCartItems(userId);
    }

    @PutMapping("/{id}")
    public void updateCartItem(@RequestBody CartRequest cartRequest,@PathVariable long id){
        cartService.updateCartItem(cartRequest,id);
    }

    @GetMapping
    public List<CartResponse> getCartProducts(@ExtractUserId String userId
            , @RequestHeader("Authorization") String authorization){
        return cartService.getCartProducts(userId,authorization);
    }

    @PatchMapping("/{cartId}/increase")
    public void increaseNumberOfItems(@PathVariable long cartId){
        cartService.increaseNumberOfItems(cartId);
    }

    @PatchMapping("/{cartId}/decrease")
    public void decreaseNumberOfItems(@PathVariable long cartId){
        cartService.decreaseNumberOfItems(cartId);
    }

}
