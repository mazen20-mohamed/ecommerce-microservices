package com.example.SaleService.controller;


import com.example.SaleService.dto.FlashSaleRequest;
import com.example.SaleService.dto.FlashSaleResponse;
import com.example.SaleService.dto.PagedResponse;
import com.example.SaleService.repository.FlashSaleRepository;
import com.example.SaleService.service.FlashSaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/flashSale")
@RequiredArgsConstructor
public class FlashSaleController {
    private final FlashSaleService flashSaleService;


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void createFlashSale(FlashSaleRequest flashSaleRequest){
        flashSaleService.createFlashSale(flashSaleRequest);
    }


    @GetMapping("/current")
    public FlashSaleResponse getTheCurrentFlashSale(){
        return flashSaleService.getTheCurrentFlashSale();
    }

    @GetMapping("/{page}/{size}")
    public PagedResponse<FlashSaleResponse> getAllFlashSales(@PathVariable int page,@PathVariable int size){
        return flashSaleService.getAllFlashSales(page,size);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteFlashSale(@PathVariable long id){
        flashSaleService.deleteFlashSale(id);
    }

    @GetMapping("/{id}")
    public FlashSaleResponse getFlashSaleById(@PathVariable long id){
        return flashSaleService.getFlashSaleById(id);
    }

}
