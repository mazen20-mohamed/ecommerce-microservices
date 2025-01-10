package com.mazen.OrderService.model.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "billing_details")
public class BillingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String streetAddress;

    private String apartmentNumber;

    private String city;

    private String government;

    private String phoneNumber;

    private String userId;

    @OneToOne(mappedBy = "billingDetails")
    private Order order;
}
