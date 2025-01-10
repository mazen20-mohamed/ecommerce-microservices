package com.mazen.OrderService.model.order;

import com.mazen.OrderService.model.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private double totalPrice;

    private String userId;

    @OneToMany(mappedBy = "order",cascade = CascadeType.PERSIST)
    private List<ProductItem> productItems;

    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "billing_details_id",referencedColumnName = "id")
    private BillingDetails billingDetails;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;
}
