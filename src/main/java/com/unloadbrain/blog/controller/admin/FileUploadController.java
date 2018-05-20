package com.unloadbrain.blog.controller.admin;

import com.unloadbrain.blog.dto.FileIdentityDTO;
import com.unloadbrain.blog.service.StorageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@AllArgsConstructor
@Controller
public class FileUploadController {

    private final StorageService storageService;

    @PostMapping("/admin/files")
    public ResponseEntity<FileIdentityDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(storageService.store(file), HttpStatus.CREATED);
    }

}
