package com.mazen.Cart.And.WishList.Service.service;

import com.mazen.Cart.And.WishList.Service.dto.CartRequest;
import com.mazen.Cart.And.WishList.Service.dto.ProductResponse;
import com.mazen.Cart.And.WishList.Service.dto.WishListRequest;
import com.mazen.Cart.And.WishList.Service.dto.WishlistResponse;
import com.mazen.Cart.And.WishList.Service.exceptions.BadRequestException;
import com.mazen.Cart.And.WishList.Service.exceptions.NotFoundException;
import com.mazen.Cart.And.WishList.Service.model.WishList;
import com.mazen.Cart.And.WishList.Service.repository.WishListRepository;
import com.mazen.Cart.And.WishList.Service.service.feignClient.ProductClient;
import com.mazen.Cart.And.WishList.Service.service.feignClient.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final UserClient userClient;
    private final ProductClient productClient;

    private WishList findByProductIdAndUserId(String productId,String userId){
        return wishListRepository.findByProductIdAndUserId
                (productId,userId).orElseThrow(()->new
                NotFoundException("Not found the product with id "+ productId));
    }

    public WishList findByWishListId(long wishlistId){
        return wishListRepository.findById(wishlistId).orElseThrow(()->
                new NotFoundException("Not found wishlist with id "+wishlistId));
    }

    @Transactional
    public void createWishList(WishListRequest wishListRequest,String authorization){
        WishList wishList = modelMapper.map(wishListRequest,WishList.class);

       Optional<WishList> wishList1 = wishListRepository.findByProductIdAndUserId(wishListRequest.getProduct_id(),wishListRequest.getUser_id());

       if(wishList1.isPresent()){
           throw new BadRequestException("The product is already in the wishlist");
       }

        // check user id and product id
        userClient.getUser(wishListRequest.getUser_id(),authorization);
        productClient.getProductDetailsById(wishListRequest.getProduct_id(),authorization);

        wishListRepository.save(wishList);
    }



    @Transactional
    public void deleteWishListProduct(long wishlistId){
        WishList wishList = findByWishListId(wishlistId);

        wishListRepository.delete(wishList);
    }

    @Transactional
    public void updateWishList(WishListRequest wishListRequest, long id){
        WishList wishList = findByWishListId(id);
        modelMapper.map(wishListRequest,wishList);
        wishListRepository.save(wishList);
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
                .user_id(wishList.getUser_id())
                .numberOfItems(1)
                .product_id(wishList.getProduct_id())
                .build());
    }

}
