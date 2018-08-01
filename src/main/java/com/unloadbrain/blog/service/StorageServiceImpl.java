package com.unloadbrain.blog.service;

import com.unloadbrain.blog.dto.FileIdentityDTO;
import com.unloadbrain.blog.exception.BadUrlException;
import com.unloadbrain.blog.exception.FileNotFoundException;
import com.unloadbrain.blog.exception.IORuntimeException;
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

/**
 * Load and save resources from file system.
 */
@Service
public class StorageServiceImpl implements StorageService {

    /**
     * Constant for user's home directory key
     */
    private static final String USER_HOME = "user.home";

    /**
     * OpenBlog home directory under user home
     */
    private static final String OPENBLOG_HOME = ".openblog";

    /**
     * Directory name where files will be stored under OpenBlog home directory.
     */
    private static final String FILES_DIR = "files";

    /**
     * File resource URL prefix
     */
    private static final String FILE_URL_PREFIX = "files";

    /**
     * Load resource from file system.
     * @param url the URL for the resource.
     * @return the resource handle for the relative resource.
     * @throws BadUrlException when provided URL is malformed.
     * @throws FileNotFoundException when file is not exist in file system.
     */
    @Override
    public Resource loadFileAsResource(String url) {

        Path filePath = Paths.get(String.format("%s/%s/%s/%s",
                System.getProperty(USER_HOME), OPENBLOG_HOME, FILES_DIR, url));

        Resource resource = loadResource(url, filePath);
        if (resource.exists()) {
            return resource;
        }

        throw new FileNotFoundException(String.format("Requested file [%s] not found", url));

    }

    /**
     * Save resource to file system.
     * @param file a MultipartFile.
     * @return identity object of newly created file.
     * @throws IORuntimeException when file can not be written in the file system.
     */
    @Override
    public FileIdentityDTO store(MultipartFile file) {

        Calendar calendar = Calendar.getInstance();

        Path uploadPath = Paths.get(String.format("%s/%s/%s/%d/%d/%d",
                System.getProperty(USER_HOME),
                OPENBLOG_HOME,
                FILES_DIR,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE)));

        createUploadDirectoryIfNotExist(uploadPath);

        String newFileName = SlugUtil.toSlug(file.getOriginalFilename());

        Path path = Paths.get(uploadPath.toString(), newFileName);

        writeFileInFileSystem(file, path);

        return new FileIdentityDTO(String.format("%s/%d/%d/%d/%s",
                FILE_URL_PREFIX,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DATE),
                newFileName)
        );
    }

    private Resource loadResource(String url, Path filePath) {
        try {
            return new UrlResource(filePath.toUri());
        } catch (MalformedURLException ex) {
            throw new BadUrlException(String.format("Provided URL [%s] is malformed.", url));
        }
    }

    private void writeFileInFileSystem(MultipartFile file, Path path) {
        try {
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
        } catch (IOException ex) {
            throw new IORuntimeException("Could not write file");
        }
    }

    private void createUploadDirectoryIfNotExist(Path uploadPath) {
        if (!uploadPath.toFile().exists()) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new IORuntimeException("Could not create storage directory.");
            }
        }
    }
}
