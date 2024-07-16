package com.mazen.OrderService.service;

import com.mazen.OrderService.dto.ProductResponse;
import com.mazen.OrderService.exceptions.NotFoundException;
import com.mazen.OrderService.model.ProductItem;
import com.mazen.OrderService.repository.ProductItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductItemService {
    private final ProductItemRepository productItemRepository;
    private final RestTemplateService restTemplateService;
    public ProductItem getProductItemByIdWithCheck(long id){
        return productItemRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Not found product of the order"));
    }

    public List<ProductResponse> getProductItemsById(List<Long> ids){
        List<ProductItem> productItems =  ids.stream()
                .map(this::getProductItemByIdWithCheck).toList();
        List<String> productIds = productItems.stream()
                .map(ProductItem::getProduct_id).toList();
        List<ProductResponse> productResponses = restTemplateService
                .getProductsByIds(productIds);
        final int[] cnt = {0};
        productResponses.forEach(productResponse -> {
            productResponse.setNumberOfItems(productItems.get(cnt[0]).getNumberOfItems());
            cnt[0]++;
        });
        return productResponses;
    }
}
