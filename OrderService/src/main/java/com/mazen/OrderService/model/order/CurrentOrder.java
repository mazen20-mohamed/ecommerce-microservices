package com.mazen.OrderService.model.order;
import com.mazen.OrderService.model.*;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "orders")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentOrder extends Order {

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

}
