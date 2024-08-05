package com.mazen.ProductService.service;


import com.mazen.ProductService.exceptions.ServerErrorException;
import com.mazen.ProductService.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<String> saveImagesRequest(List<MultipartFile> files, Product product1) throws IOException {
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

        String serverUrl = "http://localhost:8300/v1/file/"+product1.getId();

        ResponseEntity<List<String>> response = restTemplate.exchange(
                serverUrl,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<String>>() {}
        );
        if(!response.getStatusCode().equals(HttpStatus.OK)){
            log.error("Error with server when saving images");
            throw new ServerErrorException("Error with server when saving images");
        }
        return response.getBody();
    }
}
