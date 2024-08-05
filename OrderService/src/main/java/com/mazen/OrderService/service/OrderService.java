package com.mazen.OrderService.service;

import com.mazen.OrderService.dto.OrderRequest;
import com.mazen.OrderService.dto.OrderResponse;
import com.mazen.OrderService.dto.PagedResponse;
import com.mazen.OrderService.dto.ProductRequest;
import com.mazen.OrderService.exceptions.NotFoundException;
import com.mazen.OrderService.model.*;
import com.mazen.OrderService.model.order.Order;
import com.mazen.OrderService.repository.OrderRepository;
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

    public OrderResponse returnOrderResponse(Order order){
        OrderResponse orderResponse =  modelMapper.map(order,OrderResponse.class);
        orderResponse.setProductItemsId(order.getProductItems().stream().map(ProductItem::getId).toList());
        return orderResponse;
    }

    public OrderResponse getOrderById(String orderId){
        Order order = getOrderByIdWithCheck(orderId);
        return returnOrderResponse(order);
    }



    public List<OrderResponse> getOrderByUserId(String userId){
        Optional<List<Order>> orders = orderRepository.getOrdersByUserId(userId);
        return orders.map(orderList -> orderList.stream().map(this::returnOrderResponse).toList()).orElseGet(List::of);
    }

    public PagedResponse<OrderResponse> getAllOrders(int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderResponse> orderResponses =  orders.stream().map(this::returnOrderResponse).toList();
        return new PagedResponse<>(orderResponses,
                page,size,orders.getTotalElements(),
                orders.getTotalPages(),orders.isLast());
    }

    public PagedResponse<OrderResponse> getAllOrderByStatus(int page,int size,Status status){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findAllByStatus(pageable,status);
        List<OrderResponse> orderResponses =  orders.stream().map(this::returnOrderResponse).toList();
        return new PagedResponse<>(orderResponses,
                page,size,orders.getTotalElements(),
                orders.getTotalPages(),orders.isLast());
    }

}
