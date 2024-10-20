package com.mazen.Cart.And.WishList.Service.service;

import com.mazen.Cart.And.WishList.Service.dto.CartRequest;
import com.mazen.Cart.And.WishList.Service.dto.CartResponse;
import com.mazen.Cart.And.WishList.Service.dto.ProductResponse;
import com.mazen.Cart.And.WishList.Service.exceptions.BadRequestException;
import com.mazen.Cart.And.WishList.Service.exceptions.NotFoundException;
import com.mazen.Cart.And.WishList.Service.model.Cart;
import com.mazen.Cart.And.WishList.Service.repository.CartRepository;
import com.mazen.Cart.And.WishList.Service.service.feignClient.ProductClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;
    private final ProductClient productClient;

    public Cart getCartByProductIdAndUserId(String productId,String userId){
        return cartRepository.findByProductIdAndUserId(productId,userId).orElseThrow(()->
                new NotFoundException("Not found the product with id "+ productId));
    }

    @Transactional
    public void createCart(CartRequest cartRequest){
        Cart cart = modelMapper.map(cartRequest,Cart.class);
        cartRepository.save(cart);
    }

    @Transactional
    public void deleteCartProduct(String productId,String userId){
        Cart cart= getCartByProductIdAndUserId(productId,userId);
        cartRepository.delete(cart);
    }

    @Transactional
    public void deleteAllProductInCart(String userId){
        Optional<List<Cart>> cart = cartRepository.findByUserId(userId);
        if(cart.isEmpty()){
            return ;
        }
        cartRepository.deleteAll(cart.get());
    }

    @Transactional
    public void updateCart(CartRequest cartRequest, long id){
        Cart cart = cartRepository.findById(id).orElseThrow(()->
                new NotFoundException("Not found the wish list with id "+id));
        modelMapper.map(cartRequest,cart);
        cartRepository.save(cart);
    }

    public List<CartResponse> getCartProducts(String user_id, String authorization){
        Optional<List<Cart>> cart = cartRepository.findByUserId(user_id);

        if(cart.isEmpty()){
            return List.of();
        }

        List<String> productIds = cart.get().stream().map(Cart::getProduct_id).toList();

        List<ProductResponse> productResponses =  productClient.getProductsByIds(productIds,authorization);

        int index = 0;
        List<CartResponse> cartResponses = new ArrayList<>();
        for(Cart cart1 : cart.get()){
            cartResponses.add(CartResponse.builder()
                    .id(cart1.getId())
                    .productResponse(productResponses.get(index))
                    .numberOfItems(cart1.getNumberOfItems())
                    .build());
        }
        return cartResponses;
    }

    @Transactional
    public void increaseNumberOfItems(String productId, String userId){
        Cart cart= getCartByProductIdAndUserId(productId,userId);
        cart.setNumberOfItems(cart.getNumberOfItems()+1);
        cartRepository.save(cart);
    }

    @Transactional
    public void decreaseNumberOfItems(String productId, String userId){
        Cart cart= getCartByProductIdAndUserId(productId,userId);
        if(cart.getNumberOfItems()==0){
            throw new BadRequestException("cannot number of items are 0");
        }
        cart.setNumberOfItems(cart.getNumberOfItems()-1);
        cartRepository.save(cart);
    }

}
