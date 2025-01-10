package com.mazen.Cart.And.WishList.Service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String title;
    private double price;
    private double priceAfterDiscount;
    private String productCategory;
    private String imageUrl;
}
