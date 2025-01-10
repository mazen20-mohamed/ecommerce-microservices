package com.mazen.Cart.And.WishList.Service.kafka;


import com.mazen.Cart.And.WishList.Service.model.Cart;
import com.mazen.Cart.And.WishList.Service.model.WishList;
import com.mazen.Cart.And.WishList.Service.repository.CartRepository;
import com.mazen.Cart.And.WishList.Service.repository.WishListRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileProductConsumer {

    private final WishListRepository wishListRepository;
    private final CartRepository cartRepository;


    @KafkaListener(topics = "product-deletion",groupId = "productGroup")
    public void consumeProductDeletion(String id) throws MessagingException {
        log.info("Consuming product deletion from product service with id :: {}",id);
        // delete product from wishlist and cart...
        Optional<Cart> cart = cartRepository.findByProductId(id);
        cart.ifPresent(value -> cartRepository.deleteAll(Collections.singleton(value)));
        Optional<WishList> wishList = wishListRepository.findByProductId(id);
        wishList.ifPresent(value -> wishListRepository.deleteAll(Collections.singleton(value)));
    }
}
