package com.mazen.OrderService.model.order;
import com.mazen.OrderService.model.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class CurrentOrder extends Order {

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

}
