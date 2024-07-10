package com.mazen.FileStorage.controller;

import com.mazen.FileStorage.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/v1/file")
@RequiredArgsConstructor
public class FileStorageController {
    private final FileStorageService fileStorageService;


    @PostMapping("/{id}")
    public ResponseEntity<List<String>> addPhotosToProduct(@ModelAttribute List<MultipartFile> images, @PathVariable String id) throws IOException {

        return ResponseEntity.ok(fileStorageService.addPhotosForProduct(images,id));
    }

    @GetMapping("/{id}/{fileName}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String id,@PathVariable String fileName) throws IOException {
        Resource resource = fileStorageService.getPhoto(fileName,id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(resource.contentLength());
        return new ResponseEntity<>(resource,headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePhotos(@PathVariable String id) throws IOException {
        return ResponseEntity.ok(fileStorageService.deletePhotosOfProduct(id));
    }

}
