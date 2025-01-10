package com.mazen.OrderService.service;

import com.mazen.OrderService.dto.*;
import com.mazen.OrderService.exceptions.BadRequestException;
import com.mazen.OrderService.exceptions.NotFoundException;
import com.mazen.OrderService.kafka.OrderProducer;
import com.mazen.OrderService.model.*;
import com.mazen.OrderService.model.order.BillingDetails;
import com.mazen.OrderService.model.order.Order;
import com.mazen.OrderService.model.order.ProductItem;
import com.mazen.OrderService.repository.OrderRepository;
import com.mazen.OrderService.service.feign.ProductClient;
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
    private final OrderProducer orderProducer;
    private final ProductClient productClient;

    @Transactional
    public void createOrder(OrderRequest orderRequest,String authorization){
        // get products id
        List<String> ids = orderRequest.getProductItems().stream()
                .map(ProductRequest::getProduct_id).toList();

        // check if product is exits and get list of the products
        List<ProductResponse> products = productClient.getProductsByIds(ids,authorization);

        // calculate order price
        double orderPrice = products.stream().mapToDouble(ProductResponse::getPrice).sum();

        // Order Mapping
        Order order = modelMapper.map(orderRequest, Order.class);
        order.setTotalPrice(orderPrice);

        BillingDetails billingDetails = modelMapper.map(orderRequest.getDetailsRequest(), BillingDetails.class);
        order.setBillingDetails(billingDetails);

        List<ProductItem> productItems = orderRequest.getProductItems().stream().map(
                productItem -> {
                    ProductItem productItem1 = modelMapper.map(productItem, ProductItem.class);
                    log.info(productItem1.toString());
                    productItem1.setProductId(productItem.getProduct_id());
                    productItem1.setNumberOfItems(productItem.getNumberOfItems());
                    return productItem1;
                }
        ).toList();

        List<ProductItem> productItems1 =  productItems.stream().peek(productItem ->
                productItem.setOrder(order)).toList();

        order.setProductItems(productItems1);
        order.setStatus(OrderStatus.PACKING);

        // save order
        orderRepository.save(order);

        // send order creation to notification service
        orderProducer.sendOrderConfirmation(OrderConfirmation.builder()
                .orderPrice(orderPrice)
                .orderReference(order.getId())
                .status(OrderStatus.PACKING)
                .user_id(orderRequest.getUserId())
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

    @Transactional
    public void deleteOrder(String orderId){
        Order order = getOrderByIdWithCheck(orderId);
        orderRepository.delete(order);
    }


    public Order getOrderByIdWithCheck(String id){
        return orderRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Cannot find order with id "+id));
    }

    @Transactional
    public void changeStatusOfOrder(OrderStatus status , String orderId){
        Order order =  getOrderByIdWithCheck(orderId);
        if(order.getStatus().equals(OrderStatus.CANCELED)){

        }
        if((order.getStatus().equals(OrderStatus.DELIVERED) || order.getStatus().equals(OrderStatus.SHIPPED))
                && status.equals(OrderStatus.CANCELED)){
            throw new BadRequestException(String.format("Order status cannot change from %s to CANCELED",order.getStatus()));
        }
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public OrderResponse returnOrderResponse(Order order){
        OrderResponse orderResponse =  modelMapper.map(order,OrderResponse.class);
        orderResponse.setProductItemsId(order.getProductItems()
                .stream().map(ProductItem::getId).toList());
        return orderResponse;
    }

    public OrderResponse getOrderById(String orderId){
        Order order = getOrderByIdWithCheck(orderId);
        return returnOrderResponse(order);
    }

    public OrderResponse getOrderByIdAndStatus(String id, OrderStatus orderStatus) {
        Optional<Order> order = orderRepository.getOrdersByUserIdAndStatus(id,orderStatus);
        if(order.isEmpty()){
            throw new NotFoundException("Cannot find order with id "+id);
        }
        return returnOrderResponse(order.get());
    }

    public List<OrderResponse> getOrdersByProductId(String productId,List<OrderStatus> status) {
        if(status.isEmpty()){
            Optional<List<Order>> orders = orderRepository.getOrderByProductId(productId);
            return orders.map(orderList -> orderList.stream().map(this::returnOrderResponse).toList())
                    .orElseGet(List::of);
        }
        Optional<List<Order>> orders = orderRepository.getOrderByProductIdAndStatus(productId,status);
        return orders.map(orderList -> orderList.stream().map(this::returnOrderResponse).toList())
                .orElseGet(List::of);
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

    public PagedResponse<OrderResponse> getAllOrderByStatus(int page, int size, OrderStatus status){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Order> orders = orderRepository.findAllByStatus(pageable,status);
        List<OrderResponse> orderResponses =  orders.stream().map(this::returnOrderResponse).toList();
        return new PagedResponse<>(orderResponses,
                page,size,orders.getTotalElements(),
                orders.getTotalPages(),orders.isLast());
    }




}
