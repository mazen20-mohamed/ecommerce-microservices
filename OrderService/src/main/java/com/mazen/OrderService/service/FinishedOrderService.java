package com.mazen.OrderService.service;

import com.mazen.OrderService.dto.OrderResponse;
import com.mazen.OrderService.exceptions.BadRequestException;
import com.mazen.OrderService.exceptions.NotFoundException;
import com.mazen.OrderService.model.*;
import com.mazen.OrderService.model.order.FinishedOrder;
import com.mazen.OrderService.model.order.Order;
import com.mazen.OrderService.repository.FinishedOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FinishedOrderService {
    private final FinishedOrderRepository finishedOrderRepository;
    private final ModelMapper modelMapper;
    private final OrderService orderService;


    @Transactional
    public void changeOrderToFinished(String orderId){
        Order order = orderService.getOrderByIdWithCheck(orderId);
        if(!order.getStatus().equals(Status.Delivery)){
            throw new BadRequestException("Order is not even delivered");
        }

        FinishedOrder finishedOrder = FinishedOrder.builder()
                .paymentType(order.getPaymentType())
                .totalPrice(order.getTotalPrice())
                .user_id(order.getUser_id())
                .build();

        List<ProductItem> productItems =  order.getProductItems().stream().peek(productItem -> {
            productItem.setOrder(null);
            productItem.setFinishedOrder(finishedOrder);
        }).toList();

        finishedOrder.setProductItems(productItems);
        BillingDetails billingDetails = order.getBillingDetails();


        billingDetails.setFinishedOrder(finishedOrder);
        billingDetails.setOrder(null);
        finishedOrder.setBillingDetails(billingDetails);

        orderService.deleteOrder(orderId);
        finishedOrderRepository.save(finishedOrder);
    }

    public OrderResponse getFinishedOrderById(String orderId){
        FinishedOrder finishedOrder = finishedOrderRepository.findById(orderId)
                .orElseThrow(()->new NotFoundException("Not found finished order with id "+orderId));
        OrderResponse orderResponse =  modelMapper.map(finishedOrder,OrderResponse.class);
        orderResponse.setProductItemsId(finishedOrder.getProductItems().stream().map(ProductItem::getId).toList());
        return orderResponse;
    }

    public List<OrderResponse> getOrderByUserId(String userId){
        Optional<List<FinishedOrder>> orders = finishedOrderRepository.getOrdersByUserId(userId);
        return orders.map(orderList -> orderList.stream().map(order ->
                getFinishedOrderById(order.getId())).toList()).orElseGet(List::of);
    }

}
