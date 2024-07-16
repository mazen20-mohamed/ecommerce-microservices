package com.mazen.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailsRequest {


    private String streetAddress;

    private String apartmentNumber;

    private String city;

    private String government;

    private String phoneNumber;
}
