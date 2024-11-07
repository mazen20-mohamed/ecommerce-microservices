package com.mazen.Cart.And.WishList.Service;

import com.mazen.Cart.And.WishList.Service.exceptions.FeignErrorDecoder;
import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class CartAndWishListServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CartAndWishListServiceApplication.class, args);
	}

	@Bean
	Logger.Level feignLogger(){
		return Logger.Level.FULL;
	}

	@Bean
	public FeignErrorDecoder feignErrorDecoder(){
		return new FeignErrorDecoder();
	}
}
