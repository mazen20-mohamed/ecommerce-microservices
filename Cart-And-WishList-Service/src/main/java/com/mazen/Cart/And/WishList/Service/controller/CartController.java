package com.mazen.Cart.And.WishList.Service.controller;
import com.mazen.Cart.And.WishList.Service.dto.CartRequest;
import com.mazen.Cart.And.WishList.Service.dto.CartResponse;
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
    @PreAuthorize("hasRole('ADMIN') OR principal == #cartRequest.user_id")
    public void createCart(@RequestBody CartRequest cartRequest){
        cartService.createCart(cartRequest);
    }

    @DeleteMapping("/{productId}/{userId}")
    @PreAuthorize("hasRole('ADMIN') OR principal == #userId")
    public void deleteCartProduct(@PathVariable String productId,@PathVariable String userId){
        cartService.deleteCartProduct(productId,userId);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') OR principal == #userId")
    public void deleteAllProductInCart(@PathVariable String userId){
        cartService.deleteAllProductInCart(userId);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') OR principal == #cartRequest.user_id")
    public void updateCart(@RequestBody CartRequest cartRequest,@PathVariable long id){
        cartService.updateCart(cartRequest,id);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN') OR principal == #userId")
    public List<CartResponse> getCartProducts(@PathVariable String userId
            , @RequestHeader("Authorization") String authorization){
        return cartService.getCartProducts(userId,authorization);
    }

    @PatchMapping("/{productId}/{userId}/increase")
    @PreAuthorize("hasRole('ADMIN') OR principal == #userId")
    public void increaseNumberOfItems(@PathVariable String productId,@PathVariable String userId){
        cartService.increaseNumberOfItems(productId,userId);
    }

    @PatchMapping("/{productId}/{userId}/decrease")
    @PreAuthorize("hasRole('ADMIN') OR principal == #userId")
    public void decreaseNumberOfItems(String productId, String userId){
        cartService.decreaseNumberOfItems(productId,userId);
    }

}
