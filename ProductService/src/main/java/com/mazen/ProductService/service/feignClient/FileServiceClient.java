package com.mazen.ProductService.service.feignClient;

import com.mazen.ProductService.util.Colors;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@FeignClient(name = "FileStorage")
public interface FileServiceClient {

    @PostMapping(value = "/v1/file/{id}/{colors}", consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    List<String> addPhotosToProduct(
            @RequestPart("images") List<MultipartFile> images,
            @PathVariable String id,
            @PathVariable Colors colors,
            @RequestHeader("Authorization") String authorization) throws IOException;

    @PutMapping(value = "/{id}/{colors}", consumes = "multipart/form-data")
    List<String> updatePhotos(@RequestPart("images") List<MultipartFile> images,
                                     @PathVariable String id,
                                     @PathVariable Colors colors,
                              @RequestHeader("Authorization") String authorization) throws IOException;


    @DeleteMapping("/{id}")
    ResponseEntity<Boolean> deletePhotos(@PathVariable String id,@RequestHeader("Authorization") String authorization);
}
