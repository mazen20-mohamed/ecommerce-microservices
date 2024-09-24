package com.example.SaleService.service;

import com.example.SaleService.dto.FlashSaleRequest;
import com.example.SaleService.dto.FlashSaleResponse;
import com.example.SaleService.dto.PagedResponse;
import com.example.SaleService.dto.ProductSaleDTO;
import com.example.SaleService.exceptions.NotFoundException;
import com.example.SaleService.model.FlashSale;
import com.example.SaleService.model.ProductSale;
import com.example.SaleService.repository.FlashSaleRepository;
import com.example.SaleService.repository.ProductSaleRepository;
import com.example.SaleService.service.feignClient.ProductServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlashSaleService {
    private final FlashSaleRepository flashSaleRepository;
    private final ProductSaleRepository productSaleRepository;
    private final ModelMapper modelMapper;
    private final ProductServiceClient productServiceClient;

    private FlashSaleResponse mapFlashSale(FlashSale flashSale){
        FlashSaleResponse flashSaleResponse = modelMapper.map(flashSale,FlashSaleResponse.class);
        List<ProductSaleDTO> productSaleDTOS = flashSale.getProductSales().stream().map(
                productSale -> modelMapper.map(productSale,ProductSaleDTO.class)
        ).toList();
        flashSaleResponse.setProductSaleDTOS(productSaleDTOS);
        return flashSaleResponse;
    }

    public void createFlashSale(FlashSaleRequest flashSaleRequest){

        try{
             List<String> ids =  productServiceClient.isProductsExists(flashSaleRequest.
                     getProductSaleDTOS().stream().map(ProductSaleDTO::getProductId).toList());
             if(!ids.isEmpty()){
                 throw new NotFoundException("Products with ids not found "+ids);
             }
        }
        catch (FeignException ex){
            log.error(ex.getLocalizedMessage());
        }

        FlashSale flashSale = modelMapper.map(flashSaleRequest,FlashSale.class);

        List<ProductSale> productSales = flashSaleRequest.getProductSaleDTOS().stream().
                map(productSaleRequest -> {
                    Optional<ProductSale> productSale =
                            productSaleRepository.findById(productSaleRequest.getProductId());
                    if(productSale.isPresent()){
                        productSale.get().setDiscountPercent(productSaleRequest.getDiscount());
                        productSale.get().setFlashSale(flashSale);
                        return productSale.get();
                    }
                    return modelMapper.map(productSaleRequest, ProductSale.class);
                }).toList();

        flashSale.setProductSales(productSales);
        flashSaleRepository.save(flashSale);
    }

    public FlashSaleResponse getTheCurrentFlashSale(){
        LocalDateTime currentTime = LocalDateTime.now();
        FlashSale flashSale = flashSaleRepository.getFlashSaleBetweenStartAndEnd(currentTime)
                .orElseThrow(()->new NotFoundException("Not found Flash Sale at the current time"));

        return mapFlashSale(flashSale);
    }

    public PagedResponse<FlashSaleResponse> getAllFlashSales(int page, int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<FlashSale> flashSales = flashSaleRepository.findAll(pageable);
        List<FlashSaleResponse> flashSaleResponses =
                flashSales.stream().map(this::mapFlashSale).toList();
        return new PagedResponse<>(flashSaleResponses,page,size,flashSales.getTotalElements(),
                flashSales.getTotalPages(),flashSales.isLast());
    }

    public FlashSale getFlashSale(long id){
        Optional<FlashSale> flashSale =  flashSaleRepository.findById(id);
        if(flashSale.isEmpty()){
            throw new NotFoundException("Flash Sale not found!");
        }
        return flashSale.get();
    }

    public void deleteFlashSale(long id){
        FlashSale flashSale = getFlashSale(id);
        flashSaleRepository.delete(flashSale);
    }

    public FlashSaleResponse getFlashSaleById(long id){
        FlashSale flashSale = getFlashSale(id);
        return mapFlashSale(flashSale);
    }


}
