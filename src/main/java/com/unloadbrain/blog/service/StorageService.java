package com.unloadbrain.blog.service;

import com.unloadbrain.blog.dto.FileIdentityDTO;
import com.unloadbrain.blog.util.SlugUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

@Service
public class StorageService {


    public Resource loadFileAsResource(String url) {

        Path filePath = Paths.get(String.format("%s/%s/%s/%s",
                System.getProperty("user.home"), ".openblog", "images", url));

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                // TODO: Use custom exception
                throw new IllegalStateException("File not found.");
            }
        } catch (MalformedURLException ex) {
            // TODO: Use custom exception
            throw new IllegalStateException("File not found.");
        }
    }

    public FileIdentityDTO store(MultipartFile file) {

        Calendar calendar = Calendar.getInstance();

        Path uploadPath = Paths.get(String.format("%s/%s/%s/%d/%d/%d",
                System.getProperty("user.home"),
                ".openblog",
                "images",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)));

        if (!uploadPath.toFile().exists()) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                // TODO: Use custom exception
                throw new IllegalStateException("Could not create storate directory.");
            }
        }

        String newFileName = SlugUtil.toSlug(file.getOriginalFilename());

        Path path = Paths.get(uploadPath.toString(), newFileName);

        try {
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
        } catch (IOException ex) {
            // TODO: Use custom exception
            throw new IllegalStateException("Could not write file");
        }

        return new FileIdentityDTO(String.format("%s/%d/%d/%d/%s",
                "files",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                newFileName)
        );
    }
}
