package com.unloadbrain.blog.service;

import com.unloadbrain.blog.exception.FileNotFoundException;
import com.unloadbrain.blog.exception.IORuntimeException;
import com.unloadbrain.blog.util.FileSystemFileWriterUtil;
import com.unloadbrain.blog.util.UuidUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileSystemStorageServiceTest {

    private final FileSystemFileWriterUtil fileSystemFileWriterUtilMock;
    private final ResourceLoader resourceLoaderMock;
    private final UuidUtil uuidUtilMock;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final StorageService storageService;

    public FileSystemStorageServiceTest() {
        this.resourceLoaderMock = mock(ResourceLoader.class);
        this.fileSystemFileWriterUtilMock = mock(FileSystemFileWriterUtil.class);
        this.uuidUtilMock = mock(UuidUtil.class);
        this.storageService = new FileSystemStorageService(resourceLoaderMock, fileSystemFileWriterUtilMock, uuidUtilMock);
    }

    @Test
    public void shouldThrowFileNotFoundExceptionWhenResourceNotExist() {

        // Given
        Resource resource = new AbstractResource() {

            @Override
            public boolean exists() {
                return false;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }
        };
        when(resourceLoaderMock.getResource(anyString())).thenReturn(resource);

        thrown.expect(FileNotFoundException.class);
        thrown.expectMessage("Requested file [hello] not found");

        // When

        storageService.loadFileAsResource("hello");

        // Then
        // Expect test to be passed.

    }


    @Test
    public void shouldReturnResourceIfFileExist() throws Exception {

        // Given
        Resource resource = new AbstractResource() {

            @Override
            public boolean exists() {
                return true;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }
        };
        when(resourceLoaderMock.getResource(anyString())).thenReturn(resource);

        // When
        Resource returnedResource = storageService.loadFileAsResource("anything");

        // Then
        assertTrue(returnedResource.exists());

    }

    @Test
    public void shouldThrowIORuntimeExceptionWhenCouldNotCreateUploadDirectory() throws Exception {

        // Given

        thrown.expect(IORuntimeException.class);
        thrown.expectMessage("Could not create storage directory.");

        doThrow(new IOException()).when(fileSystemFileWriterUtilMock).createDirectories(any());

        // When

        storageService.store(mock(MultipartFile.class));

        // Then
        // Expect test to be passed.
    }
}