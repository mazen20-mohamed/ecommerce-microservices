package com.example.ReviewService.service;

import com.example.ReviewService.exceptions.NotFoundException;
import com.example.ReviewService.exceptions.ServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestTemplateService {
    private final RestTemplate restTemplate;

    public void checkProduct(String productId){
        String url = "http://localhost:8200/v1/product/check/"+productId;
        ResponseEntity<Boolean> check = restTemplate.getForEntity(url, Boolean.class);
        if(!check.getStatusCode().equals(HttpStatus.OK)){
            log.error("Error with product service inside order service calling");
            throw new ServerErrorException("Error with product service inside order service calling");
        }
        if(Objects.equals(check.getBody(), false)){
            throw new NotFoundException("Products with id "+ productId  + " are not found");
        }
    }

    public void checkUser(String user_id){
        String url = "http://localhost:8100/v1/user/check/"+user_id;
        ResponseEntity<Boolean> responseEntity =  restTemplate.getForEntity(url,Boolean.class);
        if(Objects.equals(responseEntity.getBody(),false)){
            throw new NotFoundException("User not found");
        }
    }
}
