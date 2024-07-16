package com.mazen.OrderService.model;

import com.mazen.OrderService.model.order.CanceledOrder;
import com.mazen.OrderService.model.order.FinishedOrder;
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

    private String product_id;

    private int numberOfItems;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "finished_Order_id",referencedColumnName = "id")
    private FinishedOrder finishedOrder;

    @ManyToOne
    @JoinColumn(name = "cancel_Order_id",referencedColumnName = "id")
    private CanceledOrder canceledOrder;

}
