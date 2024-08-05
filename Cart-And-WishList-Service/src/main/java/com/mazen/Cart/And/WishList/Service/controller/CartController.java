package com.mazen.Cart.And.WishList.Service.controller;

import com.mazen.Cart.And.WishList.Service.dto.CartRequest;
import com.mazen.Cart.And.WishList.Service.dto.ProductResponse;
import com.mazen.Cart.And.WishList.Service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCart(@RequestBody CartRequest cartRequest){
        cartService.createCart(cartRequest);
    }

    @DeleteMapping("/{productId}/{userId}")
    public void deleteCartProduct(@PathVariable String productId,@PathVariable String userId){
        cartService.deleteCartProduct(productId,userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteAllProductInCart(@PathVariable String userId){
        cartService.deleteAllProductInCart(userId);
    }

    @PutMapping("/{id}")
    public void updateCart(@RequestBody CartRequest cartRequest,@PathVariable long id){
        cartService.updateCart(cartRequest,id);
    }

    @GetMapping("/{user_id}")
    public List<ProductResponse> getCartProducts(@PathVariable String user_id){
        return cartService.getCartProducts(user_id);
    }

    @PatchMapping("/{productId}/{userId}/increase")
    public void increaseNumberOfItems(@PathVariable String productId,@PathVariable String userId){
        cartService.increaseNumberOfItems(productId,userId);
    }

    @PatchMapping("/{productId}/{userId}/decrease")
    public void decreaseNumberOfItems(String productId, String userId){
        cartService.decreaseNumberOfItems(productId,userId);
    }

    @GetMapping("/{productId}/{userId}")
    public ResponseEntity<Boolean> isProductInTheCart(String productId, String userId){
        return ResponseEntity.ok(cartService.isProductInTheCart(productId,userId));
    }
}
