package com.mazen.OrderService.model.order;

import com.mazen.OrderService.model.BillingDetails;
import com.mazen.OrderService.model.DateEntity;
import com.mazen.OrderService.model.PaymentType;
import com.mazen.OrderService.model.ProductItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "canceled_orders")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanceledOrder extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private double totalPrice;

    private String user_id;

    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @OneToOne
    @JoinColumn(name = "billing_details_id",referencedColumnName = "id")
    private BillingDetails billingDetails;

    @OneToMany(mappedBy = "canceledOrder")
    private List<ProductItem> productItems;

    private String descriptionOfCancel;
}
