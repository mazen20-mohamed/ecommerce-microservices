package com.mazen.Cart.And.WishList.Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
@SpringBootApplication
@EnableDiscoveryClient
public class CartAndWishListServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartAndWishListServiceApplication.class, args);
	}

}
