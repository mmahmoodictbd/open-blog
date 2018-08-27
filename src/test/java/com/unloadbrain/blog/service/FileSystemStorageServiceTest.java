package com.unloadbrain.blog.service;

import com.unloadbrain.blog.exception.FileNotFoundException;
import com.unloadbrain.blog.exception.IORuntimeException;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileSystemStorageServiceTest {

    private final ResourceLoader resourceLoaderMock;
    private final StorageService storageService;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public FileSystemStorageServiceTest() {
        this.resourceLoaderMock = mock(ResourceLoader.class);
        this.storageService = new FileSystemStorageService(resourceLoaderMock);
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

    @Ignore
    @Test
    public void shouldThrowIORuntimeExceptionIfFileCanNotWrittenInFileSystem() throws Exception {

        // Given

        thrown.expect(IORuntimeException.class);
        thrown.expectMessage("Could not write file");


        Mockito.when(Files.write(any(Path.class), any(byte[].class))).thenThrow(new IOException());


        // When

        //storageService.store(mockFile);

        // Then
        // Expect test to be passed.

    }
}