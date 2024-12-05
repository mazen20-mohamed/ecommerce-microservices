package com.mazen.OrderService.model.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "canceled_orders")
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class CanceledOrder extends Order {

    private String reasonOfCancel;
}
