package com.mazen.Cart.And.WishList.Service.service;

import com.mazen.Cart.And.WishList.Service.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestTemplateService {

    private final RestTemplate restTemplate;
    public List<ProductResponse> getProductsByIds(List<String> productIds) {
        String url = "http://localhost:8200/v1/product/ids?ids={ids}";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("ids", productIds);
        ResponseEntity<ProductResponse[]> responseEntity =
                restTemplate.getForEntity(builder.toUriString(), ProductResponse[].class);
        return Arrays.stream(Objects.requireNonNull(responseEntity.getBody())).toList();
    }
}
