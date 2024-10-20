package com.mazen.Cart.And.WishList.Service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private long id;
    private ProductResponse productResponse;
    private int numberOfItems;
}
