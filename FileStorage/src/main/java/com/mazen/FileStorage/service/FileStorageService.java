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
import java.io.IOException;
import java.net.MalformedURLException;
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

    public List<String> addPhotosForProduct(List<MultipartFile> files,String id) throws IOException {
        List<String> images = new ArrayList<>();
        Path directoryPath  = Paths.get(IMAGES_PATH+id);
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
                    .path("v1/file/"+id+"/")
                    .path(Objects.requireNonNull(f.getOriginalFilename()))
                    .toUriString();
            images.add(fileDownloadUri);

            Path fileNameAndPath = Paths.get(IMAGES_PATH+id, f.getOriginalFilename());
            Files.write(fileNameAndPath, f.getBytes());
        }
        return images;
    }


    public Resource getPhoto(String fileName,String id){
        try {
            String path = IMAGES_PATH+id+"/";
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

    public boolean deletePhotosOfProduct(String id) throws IOException {
        Path p = Paths.get(IMAGES_PATH+id);
        return Files.deleteIfExists(p);
    }
}
