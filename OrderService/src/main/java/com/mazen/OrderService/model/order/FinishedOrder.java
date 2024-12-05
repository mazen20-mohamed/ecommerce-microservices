package com.mazen.OrderService.model.order;


import com.mazen.OrderService.model.BillingDetails;
import com.mazen.OrderService.model.DateEntity;
import com.mazen.OrderService.model.PaymentType;
import com.mazen.OrderService.model.ProductItem;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "finished_orders")
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class FinishedOrder extends Order {

    private LocalDateTime endDate;
}
