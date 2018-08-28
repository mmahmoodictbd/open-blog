package com.unloadbrain.blog.service;

import com.unloadbrain.blog.dto.FileIdentityDTO;
import com.unloadbrain.blog.exception.BadUrlException;
import com.unloadbrain.blog.exception.FileNotFoundException;
import com.unloadbrain.blog.exception.IORuntimeException;
import com.unloadbrain.blog.util.FileSystemFileWriterUtil;
import com.unloadbrain.blog.util.SlugUtil;
import com.unloadbrain.blog.util.UuidUtil;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

/**
 * Load and save resources from file system.
 */
@Service
public class FileSystemStorageService implements StorageService {

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


    private final ResourceLoader resourceLoader;
    private final FileSystemFileWriterUtil fileSystemFileWriterUtil;
    private final UuidUtil uuidUtil;

    public FileSystemStorageService(ResourceLoader resourceLoader,
                                    FileSystemFileWriterUtil fileSystemFileWriterUtil,
                                    UuidUtil uuidUtil) {
        this.resourceLoader = resourceLoader;
        this.fileSystemFileWriterUtil = fileSystemFileWriterUtil;
        this.uuidUtil = uuidUtil;
    }

    /**
     * Load resource from file system.
     * @param url the URL for the resource.
     * @return the resource handle for the relative resource.
     * @throws BadUrlException when provided URL is malformed.
     * @throws FileNotFoundException when file is not exist in file system.
     */
    @Override
    public Resource loadFileAsResource(String url) {

        String filePath = String.format("file:%s/%s/%s/%s",
                System.getProperty(USER_HOME), OPENBLOG_HOME, FILES_DIR, url);

        Resource resource = resourceLoader.getResource(filePath);
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
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE)));

        createUploadDirectoryIfNotExist(uploadPath);

        String newFileName = buildNewFilename(file.getOriginalFilename());

        Path path = Paths.get(uploadPath.toString(), newFileName);

        writeFileInFileSystem(path, readFileBytes(file));

        return new FileIdentityDTO(String.format("%s/%d/%d/%d/%s",
                FILE_URL_PREFIX,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DATE),
                newFileName)
        );
    }

    private void createUploadDirectoryIfNotExist(Path uploadPath) {
        if (!uploadPath.toFile().exists()) {
            try {
                fileSystemFileWriterUtil.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new IORuntimeException("Could not create storage directory.");
            }
        }
    }

    private String buildNewFilename(String originalFilename) {
        String filenameWithoutExt = getFilenameWithoutExtension(originalFilename);
        String sluggedFilenameWithoutExt = SlugUtil.toSlug(filenameWithoutExt);
        String fileExt = getFileExtension(originalFilename);
        return uuidUtil.getUuid() + "_" + sluggedFilenameWithoutExt + fileExt;
    }

    private byte[] readFileBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException ex) {
            throw new IORuntimeException("Could not read file.");
        }
    }

    private void writeFileInFileSystem(Path path, byte[] fileBytes) {
        try {
            fileSystemFileWriterUtil.write(path, fileBytes);
        } catch (IOException ex) {
            throw new IORuntimeException("Could not write file.");
        }
    }

    private String getFilenameWithoutExtension(String filename) {
        int pos = filename.lastIndexOf(".");
        return pos > 0 ? filename.substring(0, pos) : filename;
    }

    private String getFileExtension(String filename) {
        int pos = filename.lastIndexOf(".");
        return pos > 0 ? filename.substring(pos + 1) : filename;
    }
}
