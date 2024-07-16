package com.mazen.Cart.And.WishList.Service.service;

import com.mazen.Cart.And.WishList.Service.dto.ProductResponse;
import com.mazen.Cart.And.WishList.Service.dto.WishListRequest;
import com.mazen.Cart.And.WishList.Service.exceptions.NotFoundException;
import com.mazen.Cart.And.WishList.Service.model.Cart;
import com.mazen.Cart.And.WishList.Service.model.WishList;
import com.mazen.Cart.And.WishList.Service.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ModelMapper modelMapper;
    private final RestTemplateService restTemplateService;

    @Transactional
    public void createWishList(WishListRequest wishListRequest){
        WishList wishList = modelMapper.map(wishListRequest,WishList.class);
        wishListRepository.save(wishList);
    }

    @Transactional
    public void deleteWishListProduct(String productId,String userId){
        WishList wishList = wishListRepository.findByProductIdAndUserId(productId,userId).orElseThrow(()->new NotFoundException("Not found the product with id "+ productId));

        wishListRepository.delete(wishList);
    }

    @Transactional
    public void updateWishList(WishListRequest wishListRequest, long id){
        WishList wishList = wishListRepository.findById(id).orElseThrow(()->new NotFoundException("Not found the wish list with id "+id));
        modelMapper.map(wishListRequest,wishList);
        wishListRepository.save(wishList);
    }

    public List<ProductResponse> getWishListProducts(String user_id){
        Optional<List<WishList>> wishList = wishListRepository.findByUserId(user_id);
        if(wishList.isEmpty()){
            return List.of();
        }
        List<String> productIds = wishList.get().stream().map(WishList::getProduct_id).toList();
        return restTemplateService.getProductsByIds(productIds);
    }

    public boolean isProductInTheWishList(String productId,String userId){
        Optional<WishList> wishList = wishListRepository.findByProductIdAndUserId(productId,userId);
        return wishList.isPresent();
    }


}
