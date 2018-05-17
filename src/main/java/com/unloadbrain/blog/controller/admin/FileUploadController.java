package com.unloadbrain.blog.controller.admin;

import com.unloadbrain.blog.dto.FileIdentityDTO;
import com.unloadbrain.blog.service.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Controller
public class FileUploadController {

    private final StorageService storageService;

    @PostMapping("/admin/files")
    public FileIdentityDTO handleFileUpload(@RequestParam("file") MultipartFile file) {
        return storageService.store(file);
    }

}
