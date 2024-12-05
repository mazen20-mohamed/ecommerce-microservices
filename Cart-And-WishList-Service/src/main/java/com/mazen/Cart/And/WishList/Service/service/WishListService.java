package com.mazen.Cart.And.WishList.Service.service;

import com.mazen.Cart.And.WishList.Service.dto.CartRequest;
import com.mazen.Cart.And.WishList.Service.dto.ProductResponse;
import com.mazen.Cart.And.WishList.Service.dto.WishlistResponse;
import com.mazen.Cart.And.WishList.Service.exceptions.BadRequestException;
import com.mazen.Cart.And.WishList.Service.exceptions.NotFoundException;
import com.mazen.Cart.And.WishList.Service.model.WishList;
import com.mazen.Cart.And.WishList.Service.repository.WishListRepository;
import com.mazen.Cart.And.WishList.Service.service.feignClient.ProductClient;
import com.mazen.Cart.And.WishList.Service.service.feignClient.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;
    private final CartService cartService;
    private final UserClient userClient;
    private final ProductClient productClient;

    public WishList findByWishListId(long wishlistId){
        return wishListRepository.findById(wishlistId).orElseThrow(()->
                new NotFoundException("Not found wishlist with id "+wishlistId));
    }

    @Transactional
    public void createWishList(String productId,String authorization,String userId){
        WishList wishList = WishList.builder()
                .product_id(productId)
                .user_id(userId)
                .build();

       Optional<WishList> wishList1 = wishListRepository.findByProductIdAndUserId(productId,userId);

       if(wishList1.isPresent()){
           throw new BadRequestException("The product is already in the wishlist");
       }

        // check user id and product id
        userClient.getUser(userId,authorization);
        productClient.getProductDetailsById(productId,authorization);

        wishListRepository.save(wishList);
    }



    @Transactional
    public void deleteWishListProduct(long wishlistId){
        WishList wishList = findByWishListId(wishlistId);

        wishListRepository.delete(wishList);
    }

    public List<WishlistResponse> getWishListProducts(String userId, String authorization){
        List<WishList> wishList = wishListRepository.findByUserId(userId);

        if(wishList.isEmpty()){
            return List.of();
        }
        List<String> productIds = wishList.stream().map(WishList::getProduct_id).toList();
        List<ProductResponse> productResponses = productClient.getProductsByIds(productIds,authorization);
        List<WishlistResponse> wishlistResponses = new ArrayList<>();
        int index = 0;
        for(WishList wishList1 : wishList){
            wishlistResponses.add(WishlistResponse.builder()
                    .productResponse(productResponses.get(index))
                    .id(wishList1.getId())
                    .build());
            index++;
        }
        return wishlistResponses;
    }

    @Transactional
    public void deleteWishLists(String userId){
        List<WishList> wishLists = wishListRepository.findByUserId(userId);
        if(wishLists.isEmpty()){
            return;
        }
        wishListRepository.deleteAll(wishLists);
    }

    @Transactional
    public void moveToCart(long wishlistId){
        WishList wishList =  findByWishListId(wishlistId);
        cartService.createCartItem(CartRequest.builder()
                .numberOfItems(1)
                .product_id(wishList.getProduct_id())
                .build(),wishList.getUser_id());
    }

}
