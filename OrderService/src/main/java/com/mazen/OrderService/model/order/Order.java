package com.mazen.OrderService.model.order;

import com.mazen.OrderService.model.BillingDetails;
import com.mazen.OrderService.model.DateEntity;
import com.mazen.OrderService.model.PaymentType;
import com.mazen.OrderService.model.ProductItem;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "order_type")
@Entity
@Table
public abstract class Order extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private double totalPrice;

    private String user_id;

    @OneToMany(mappedBy = "order",cascade = CascadeType.PERSIST)
    private List<ProductItem> productItems;

    @Enumerated(value = EnumType.STRING)
    private PaymentType paymentType;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "billing_details_id",referencedColumnName = "id")
    private BillingDetails billingDetails;
}
