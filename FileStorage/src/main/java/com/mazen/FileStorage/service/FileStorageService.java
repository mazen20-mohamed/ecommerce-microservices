package com.mazen.FileStorage.service;


import com.mazen.FileStorage.exceptions.BadRequestException;
import com.mazen.FileStorage.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileStorageService {
    public static String IMAGES_PATH = System.getProperty("user.dir") + "/FileStorage/public/images/";

    public List<String> addPhotosForProduct(List<MultipartFile> files,
                                            String id,
                                            Colors colors) throws IOException {
        List<String> images = new ArrayList<>();
        Path directoryPath  = Paths.get(IMAGES_PATH+id+"/"+colors.toString());
        // Ensure the directory is created
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
                log.info("Directory created: " + directoryPath.toString());
            } catch (IOException e) {
                log.error("Failed to create directory: " + directoryPath.toString());
            }
        }

        for(MultipartFile f : files){

            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("v1/file/"+id+"/"+colors+"/")
                    .path(Objects.requireNonNull(f.getOriginalFilename()))
                    .toUriString();
            images.add(fileDownloadUri);

            Path fileNameAndPath = Paths.get(IMAGES_PATH+id+"/"+colors,
                    f.getOriginalFilename());
            Files.write(fileNameAndPath, f.getBytes());
        }
        return images;
    }


    public Resource getPhoto(String fileName,String id,Colors colors){
        try {
            String path = IMAGES_PATH+id+"/"+colors+"/";
            Path filePath;

            filePath = Paths.get(path);
            filePath =filePath.resolve(fileName).normalize();

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new NotFoundException("File not found " + fileName);
            }
        }
        catch (MalformedURLException ex){
            throw new NotFoundException("File not found " + fileName);
        }
    }

    public boolean deletePhotosOfProduct(String id){
        Path p = Paths.get(IMAGES_PATH+id);
        try{
            deleteDirectoryRecursively(p);
            log.info("Deleted images");
        }
        catch (Exception e){
            log.error("Failed to delete images");
        }
        return true;
    }
    public static void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
                for (Path entry : entries) {
                    deleteDirectoryRecursively(entry);
                }
            }
        }
        Files.delete(path);
    }

    public List<String> updatePhotos(List<MultipartFile> files,
                                     String id,Colors colors) throws IOException {
        deletePhotosOfProduct(id);
        return addPhotosForProduct(files,id,colors);
    }
}
