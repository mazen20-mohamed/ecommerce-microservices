package com.mazen.Cart.And.WishList.Service.model;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "wishlists")
public class WishList extends DateEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String product_id;

    private String user_id;
}
