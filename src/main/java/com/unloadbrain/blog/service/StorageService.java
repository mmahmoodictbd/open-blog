package com.unloadbrain.blog.service;

import com.unloadbrain.blog.dto.FileIdentityDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    Resource loadFileAsResource(String url);

    FileIdentityDTO store(MultipartFile file);
}
