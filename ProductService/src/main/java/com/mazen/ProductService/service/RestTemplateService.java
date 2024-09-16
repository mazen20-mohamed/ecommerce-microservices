package com.mazen.ProductService.service;

import com.mazen.ProductService.exceptions.ServerErrorException;
import com.mazen.ProductService.model.Product;
import com.mazen.ProductService.util.Colors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestTemplateService {
    private final RestTemplate restTemplate;

    public void deleteProductImages(String id){
        String serverUrl = "http://localhost:8300/v1/file/"+id;
        ResponseEntity<Boolean> response = restTemplate.exchange(
                serverUrl,
                HttpMethod.DELETE,
                null,
                new ParameterizedTypeReference<Boolean>() {}
        );
        if(!response.getStatusCode().equals(HttpStatus.OK)){
            log.error("Error with server when deleting images");
            throw new ServerErrorException("Error with server when deleting images");
        }
    }

    private List<String> saveAndUpdateImageRequest
            (List<MultipartFile> files , Product product1 ,Colors colors ,HttpMethod httpStatus) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        for(MultipartFile f : files){
            ByteArrayResource resource = new ByteArrayResource(f.getBytes()) {
                @Override
                public String getFilename() {
                    return f.getOriginalFilename();
                }
            };
            body.add("images", resource);
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        String serverUrl = "http://localhost:8300/v1/file/"+product1.getId()+"/"+colors;

        ResponseEntity<List<String>> response = restTemplate.exchange(
                serverUrl,
                httpStatus,
                requestEntity,
                new ParameterizedTypeReference<List<String>>() {}
        );
        if(!response.getStatusCode().equals(HttpStatus.OK)){
            log.error("Error with server when saving images");
            throw new ServerErrorException("Error with server when saving images");
        }
        return response.getBody();
    }

    public List<String> saveImagesRequest(List<MultipartFile> files, Product product1, Colors colors) throws IOException {
        return saveAndUpdateImageRequest(files,product1, colors , HttpMethod.POST);
    }

    public List<String> updateImagesRequest(List<MultipartFile> files,
                                            Product product1 , Colors colors) throws IOException{
        return saveAndUpdateImageRequest(files,product1, colors ,HttpMethod.PUT);
    }

    public int getDiscountOfProduct(String productId){
        ResponseEntity<Integer> discount = restTemplate
                .getForEntity("http://localhost:8600/v1/productSale/"+productId,Integer.class);
        return discount.getBody();
    }
}
