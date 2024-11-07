package com.mazen.OrderService.service;

import com.mazen.OrderService.dto.*;
import com.mazen.OrderService.exceptions.NotFoundException;
import com.mazen.OrderService.kafka.OrderProducer;
import com.mazen.OrderService.model.*;
import com.mazen.OrderService.model.order.CurrentOrder;
import com.mazen.OrderService.repository.CurrentOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final CurrentOrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final RestTemplateService restTemplateService;
    private final OrderProducer orderProducer;

    @Transactional
    public void createOrder(OrderRequest orderRequest){
        // get products id
        List<String> ids = orderRequest.getProductItems().stream()
                .map(ProductRequest::getProduct_id).toList();

        // check if product is exits
        restTemplateService.isProductIdExits(ids);

        // calculate order price
        double orderPrice = restTemplateService.calculateOrderPrice(ids);

        // Order Mapping
        CurrentOrder order = modelMapper.map(orderRequest, CurrentOrder.class);
        order.setTotalPrice(orderPrice);

        BillingDetails billingDetails = modelMapper.map(orderRequest.getDetailsRequest(), BillingDetails.class);
        order.setBillingDetails(billingDetails);

        List<ProductItem> productItems = orderRequest.getProductItems().stream().map(
                productItem -> modelMapper.map(productItem, ProductItem.class)
        ).toList();

        List<ProductItem> productItems1 =  productItems.stream().peek(productItem -> productItem.setOrder(order)).toList();

        order.setProductItems(productItems1);
        order.setStatus(OrderStatus.Packing);

        // save order
        orderRepository.save(order);

        // send order creation to notification service
        orderProducer.sendOrderConfirmation(OrderConfirmation.builder()
                .orderPrice(orderPrice)
                .orderReference(order.getId())
                .status(OrderStatus.Packing)
                .user_id(orderRequest.getUser_id())
                .detailsShippingResponse(
                        DetailsShippingResponse
                                .builder().id(billingDetails.getId())
                                .city(billingDetails.getCity())
                                .apartmentNumber(billingDetails.getApartmentNumber())
                                .phoneNumber(billingDetails.getPhoneNumber())
                                .government(billingDetails.getGovernment())
                                .build()
                ).build());
    }

    public CurrentOrder getOrderByIdWithCheck(String id){
        return orderRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot find order with id "+id));
    }

    @Transactional
    public void changeStatusOfOrder(OrderStatus status , String orderId){
        CurrentOrder order =  getOrderByIdWithCheck(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public void deleteOrder(String orderId){
        CurrentOrder order = getOrderByIdWithCheck(orderId);
        orderRepository.delete(order);
    }


    @Transactional
    public OrderResponse returnOrderResponse(CurrentOrder order){
        OrderResponse orderResponse =  modelMapper.map(order,OrderResponse.class);
        orderResponse.setProductItemsId(order.getProductItems()
                .stream().map(ProductItem::getId).toList());
        return orderResponse;
    }

    public OrderResponse getOrderById(String orderId){
        CurrentOrder order = getOrderByIdWithCheck(orderId);
        return returnOrderResponse(order);
    }



    public List<OrderResponse> getOrderByUserId(String userId){
        Optional<List<CurrentOrder>> orders = orderRepository.getOrdersByUserId(userId);
        return orders.map(orderList -> orderList.stream().map(this::returnOrderResponse).toList()).orElseGet(List::of);
    }

    public PagedResponse<OrderResponse> getAllOrders(int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<CurrentOrder> orders = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses =  orders.stream().map(this::returnOrderResponse).toList();
        return new PagedResponse<>(orderResponses,
                page,size,orders.getTotalElements(),
                orders.getTotalPages(),orders.isLast());
    }

    public PagedResponse<OrderResponse> getAllOrderByStatus(int page, int size, OrderStatus status){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<CurrentOrder> orders = orderRepository.findAllByStatus(pageable,status);
        List<OrderResponse> orderResponses =  orders.stream().map(this::returnOrderResponse).toList();
        return new PagedResponse<>(orderResponses,
                page,size,orders.getTotalElements(),
                orders.getTotalPages(),orders.isLast());
    }

}
