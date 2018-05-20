package com.unloadbrain.blog.controller;

import com.unloadbrain.blog.service.StorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@AllArgsConstructor
@Controller
public class FileStorageController {

    private final StorageService storageService;

    @GetMapping("/files/**")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {

        String url = request.getRequestURL().toString().split("/files/")[1];
        if (url == null || url.trim().length() == 0) {
            // TODO: use custom exception
            throw new IllegalArgumentException("Bad URL.");
        }

        Resource resource = storageService.loadFileAsResource(url);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

}
