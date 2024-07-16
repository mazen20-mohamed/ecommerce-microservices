package com.mazen.OrderService.service;

import com.mazen.OrderService.dto.OrderRequest;
import com.mazen.OrderService.dto.OrderResponse;
import com.mazen.OrderService.dto.ProductRequest;
import com.mazen.OrderService.exceptions.NotFoundException;
import com.mazen.OrderService.model.*;
import com.mazen.OrderService.model.order.Order;
import com.mazen.OrderService.repository.OrderRepository;
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
public class OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final RestTemplateService restTemplateService;

    @Transactional
    public void createOrder(OrderRequest orderRequest){
        List<String> ids = orderRequest.getProductItems().stream()
                .map(ProductRequest::getProduct_id).toList();

        restTemplateService.isProductIdExits(ids);

        Order order = modelMapper.map(orderRequest,Order.class);

        BillingDetails billingDetails = modelMapper.map(orderRequest.getDetailsRequest(), BillingDetails.class);
        order.setBillingDetails(billingDetails);

        List<ProductItem> productItems = orderRequest.getProductItems().stream().map(
                productItem -> modelMapper.map(productItem, ProductItem.class)
        ).toList();

        List<ProductItem> productItems1 =  productItems.stream().peek(productItem -> productItem.setOrder(order)).toList();

        order.setProductItems(productItems1);
        order.setStatus(Status.Packing);
        orderRepository.save(order);
    }

    public Order getOrderByIdWithCheck(String id){
        return orderRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot find order with id "+id));
    }

    @Transactional
    public void changeStatusOfOrder(Status status , String orderId){
        Order order =  getOrderByIdWithCheck(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(String orderId){
        Order order = getOrderByIdWithCheck(orderId);
        orderRepository.delete(order);
    }

    public OrderResponse getOrderById(String orderId){
        Order order = getOrderByIdWithCheck(orderId);
        OrderResponse orderResponse =  modelMapper.map(order,OrderResponse.class);
        orderResponse.setProductItemsId(order.getProductItems().stream().map(ProductItem::getId).toList());
        return orderResponse;
    }


    public List<OrderResponse> getOrderByUserId(String userId){
        Optional<List<Order>> orders = orderRepository.getOrdersByUserId(userId);
        return orders.map(orderList -> orderList.stream().map(order ->
                getOrderById(order.getId())).toList()).orElseGet(List::of);
    }

}
