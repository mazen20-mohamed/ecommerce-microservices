package com.mazen.OrderService.service;

import com.mazen.OrderService.dto.DetailsShippingResponse;
import com.mazen.OrderService.dto.UserResponseDTO;
import com.mazen.OrderService.exceptions.NotFoundException;
import com.mazen.OrderService.model.BillingDetails;
import com.mazen.OrderService.repository.BillingDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BillingDetailsService {
    private final BillingDetailsRepository billingDetailsRepository;
    private final ModelMapper modelMapper;
    private final RestTemplateService restTemplateService;

//    public DetailsShippingResponse getShippingDetailsOfOrder(String orderId){
//
//    }

    public DetailsShippingResponse getShippingDetailsById(long id){
        BillingDetails billingDetails = getBillingDetailsById(id);
        UserResponseDTO userResponseDTO;
        if(billingDetails.getOrder()==(null)){
            userResponseDTO = restTemplateService.getUserData(billingDetails.getFinishedOrder().getUser_id());
        }
        else{
            userResponseDTO = restTemplateService.getUserData(billingDetails.getOrder().getUser_id());
        }
        DetailsShippingResponse detailsShippingResponse =modelMapper.map(billingDetails,DetailsShippingResponse.class);
        detailsShippingResponse.setUserName(userResponseDTO.getFirstName() + " "+userResponseDTO.getLastName());
        return detailsShippingResponse;
    }

    public BillingDetails getBillingDetailsById(long id){
        return billingDetailsRepository.findById(id).orElseThrow(()->
                new NotFoundException("Not found details with id "+id));
    }

}