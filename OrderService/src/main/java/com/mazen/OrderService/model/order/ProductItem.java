package com.mazen.OrderService.model.order;
import com.mazen.OrderService.model.order.Order;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "product_item")
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String productId;

    private int numberOfItems;

    @ManyToOne
    @JoinColumn(name = "orderId",referencedColumnName = "id")
    private Order order;

}
