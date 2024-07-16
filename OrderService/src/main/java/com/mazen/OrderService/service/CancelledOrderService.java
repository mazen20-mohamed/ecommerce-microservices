package com.mazen.OrderService.service;


import com.mazen.OrderService.dto.CancelOrderResponse;
import com.mazen.OrderService.dto.OrderResponse;
import com.mazen.OrderService.exceptions.BadRequestException;
import com.mazen.OrderService.exceptions.NotFoundException;
import com.mazen.OrderService.model.BillingDetails;
import com.mazen.OrderService.model.ProductItem;
import com.mazen.OrderService.model.Status;
import com.mazen.OrderService.model.order.CanceledOrder;
import com.mazen.OrderService.model.order.FinishedOrder;
import com.mazen.OrderService.model.order.Order;
import com.mazen.OrderService.repository.CancelledOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CancelledOrderService {
    private final CancelledOrderRepository cancelledOrderRepository;
    private final OrderService orderService;
    private final ModelMapper modelMapper;


    public void changeOrderToCancel(String orderId,String reason){
        Order order = orderService.getOrderByIdWithCheck(orderId);
        if(!order.getStatus().equals(Status.Packing)){
            throw new BadRequestException("Cannot cancel order request is being shipped, please call us...");
        }

        CanceledOrder canceledOrder = CanceledOrder.builder()
                .paymentType(order.getPaymentType())
                .totalPrice(order.getTotalPrice())
                .user_id(order.getUser_id())
                .descriptionOfCancel(reason)
                .build();

        List<ProductItem> productItems =  order.getProductItems().stream().peek(productItem -> {
            productItem.setOrder(null);
            productItem.setCanceledOrder(canceledOrder);
        }).toList();

        canceledOrder.setProductItems(productItems);
        BillingDetails billingDetails = order.getBillingDetails();


        billingDetails.setCanceledOrder(canceledOrder);
        billingDetails.setOrder(null);
        canceledOrder.setBillingDetails(billingDetails);

        orderService.deleteOrder(orderId);
        cancelledOrderRepository.save(canceledOrder);
    }

    public CancelOrderResponse getCancelledOrderById(String orderId){
        CanceledOrder canceledOrder = cancelledOrderRepository.findById(orderId)
                .orElseThrow(()->new NotFoundException("Not found finished order with id "+orderId));
        CancelOrderResponse orderResponse =  modelMapper.map(canceledOrder,CancelOrderResponse.class);
        orderResponse.setProductItemsId(canceledOrder.getProductItems().stream().map(ProductItem::getId).toList());
        return orderResponse;
    }

    public List<CancelOrderResponse> getOrderByUserId(String userId){
        Optional<List<CanceledOrder>> orders = cancelledOrderRepository.getOrdersByUserId(userId);
        return orders.map(orderList -> orderList.stream().map(order ->
                getCancelledOrderById(order.getId())).toList()).orElseGet(List::of);
    }
}
