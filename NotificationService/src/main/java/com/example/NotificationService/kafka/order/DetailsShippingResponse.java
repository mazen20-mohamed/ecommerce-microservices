package com.example.NotificationService.kafka.order;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailsShippingResponse {
    private long id;
    private String userName;
    private String streetAddress;
    private String apartmentNumber;
    private String city;
    private String government;
    private String phoneNumber;
}
