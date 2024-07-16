package com.mazen.OrderService.service;

import com.mazen.OrderService.dto.ProductResponse;
import com.mazen.OrderService.dto.UserResponseDTO;
import com.mazen.OrderService.exceptions.NotFoundException;
import com.mazen.OrderService.exceptions.ServerErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestTemplateService {
    private final RestTemplate restTemplate;

    public void isProductIdExits(List<String> ids){
        String url = "http://localhost:8200/v1/product/isProductsExists?ids={ids}";
        ResponseEntity<Boolean> check = restTemplate.getForEntity(url, Boolean.class,String.join(",", ids));
        if(!check.getStatusCode().equals(HttpStatus.OK)){
            log.error("Error with product service inside order service calling");
            throw new ServerErrorException("Error with product service inside order service calling");
        }
        if(Objects.equals(check.getBody(), false)){
            throw new NotFoundException("Products with ids "+ ids  + " are not found");
        }
    }

    public List<ProductResponse> getProductsByIds(List<String> ids) {
        try{
            String url = "http://localhost:8200/v1/product/ids?ids={ids}";
            ResponseEntity<ProductResponse[]> responseEntity =
                    restTemplate.getForEntity(url, ProductResponse[].class,String.join(",", ids));
            return Arrays.stream(Objects.requireNonNull(responseEntity.getBody())).toList();
        }
        catch (Exception e){
            throw new NotFoundException("Not found products with ids "+ids.toString());
        }
    }

    public UserResponseDTO getUserData(String userId){
        try{
            String url = "http://localhost:8100/v1/user/"+userId;
            ResponseEntity<UserResponseDTO> responseEntity =
                    restTemplate.getForEntity(url, UserResponseDTO.class);
            return responseEntity.getBody();
        }
        catch (Exception e){
            throw new NotFoundException("Not found user with id "+userId);
        }
    }

}
