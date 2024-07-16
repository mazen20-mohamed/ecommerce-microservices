package com.mazen.Cart.And.WishList.Service.model;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "carts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends DateEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String product_id;

    private String user_id;
    private int numberOfItems;
}
