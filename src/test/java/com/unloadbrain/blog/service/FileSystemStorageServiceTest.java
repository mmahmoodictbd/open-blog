package com.unloadbrain.blog.service;

import com.unloadbrain.blog.exception.FileNotFoundException;
import com.unloadbrain.blog.exception.IORuntimeException;
import com.unloadbrain.blog.util.DateUtil;
import com.unloadbrain.blog.util.FileSystemFileWriterUtil;
import com.unloadbrain.blog.util.OpenBlogSystemUtil;
import com.unloadbrain.blog.util.UuidUtil;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileSystemStorageServiceTest {

    private final MultipartFile fileMock;
    private final FileSystemFileWriterUtil fileSystemFileWriterUtilMock;
    private final ResourceLoader resourceLoaderMock;
    private final DateUtil dateUtilMock;
    private final UuidUtil uuidUtilMock;
    private final OpenBlogSystemUtil openBlogSystemUtilMock;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final StorageService storageService;

    public FileSystemStorageServiceTest() {
        this.fileMock = mock(MultipartFile.class);
        this.resourceLoaderMock = mock(ResourceLoader.class);
        this.dateUtilMock = mock(DateUtil.class);
        this.uuidUtilMock = mock(UuidUtil.class);
        this.openBlogSystemUtilMock = mock(OpenBlogSystemUtil.class);
        this.fileSystemFileWriterUtilMock = mock(FileSystemFileWriterUtil.class);
        this.storageService = new FileSystemStorageService(resourceLoaderMock,
                dateUtilMock,
                uuidUtilMock,
                openBlogSystemUtilMock,
                fileSystemFileWriterUtilMock);
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
    public void shouldLookForResourceInOpenBlogHomeFilesDirectory() {

        // Given

        ArgumentCaptor<String> filePathCaptor = ArgumentCaptor.forClass(String.class);

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

        when(openBlogSystemUtilMock.getOpenBlogHome()).thenReturn("/home/user/.openblog");

        // When
        storageService.loadFileAsResource("anything");

        // Then
        verify(resourceLoaderMock).getResource(filePathCaptor.capture());
        assertEquals("file:/home/user/.openblog/files/anything", filePathCaptor.getValue().toString());
    }

    @Test
    public void shouldThrowIORuntimeExceptionWhenCouldNotCreateUploadDirectory() throws Exception {

        // Given

        thrown.expect(IORuntimeException.class);
        thrown.expectMessage("Could not create storage directory.");

        doThrow(new IOException()).when(fileSystemFileWriterUtilMock).createDirectories(any());

        // When

        storageService.store(fileMock);

        // Then
        // Expect test to be passed.
    }

    @Test
    public void shouldThrowIORuntimeExceptionWhenFileBytesCouldNotBeRead() throws Exception {

        // Given

        thrown.expect(IORuntimeException.class);
        thrown.expectMessage("Could not read file.");

        when(fileMock.getOriginalFilename()).thenReturn("my picture.jpg");
        doThrow(new IOException()).when(fileMock).getBytes();

        // When
        storageService.store(fileMock);

        // Then
        // Expect test to be passed.
    }

    @Test
    public void shouldThrowIORuntimeExceptionWhenFileCanNotBeWritten() throws Exception {

        // Given

        thrown.expect(IORuntimeException.class);
        thrown.expectMessage("Could not write file.");

        when(fileMock.getOriginalFilename()).thenReturn("my picture.jpg");
        doThrow(new IOException()).when(fileSystemFileWriterUtilMock).write(any(), any());

        // When
        storageService.store(fileMock);

        // Then
        // Expect test to be passed.
    }

    @Test
    public void shouldCreateUploadDirectoryUsingCurrentDate() throws Exception {

        // Given

        ArgumentCaptor<Path> pathArgumentCaptor = ArgumentCaptor.forClass(Path.class);

        when(fileMock.getOriginalFilename()).thenReturn("my picture.jpg");
        when(dateUtilMock.getCurrentYearMonthDateString()).thenReturn("2018/8/29");
        when(openBlogSystemUtilMock.getOpenBlogHome()).thenReturn("/home/user/.openblog");

        // When
        storageService.store(fileMock);

        // Then
        verify(fileSystemFileWriterUtilMock).createDirectories(pathArgumentCaptor.capture());

        if ("/".equals(File.separator)) {
            assertEquals("/home/user/.openblog/files/2018/8/29", pathArgumentCaptor.getValue().toString());
        } else {
            assertEquals("\\home\\user\\.openblog\\files\\2018\\8\\29", pathArgumentCaptor.getValue().toString());
        }
    }

    @Test
    public void shouldFileNameBeGeneratedFromUuidAndSluggedFileName() throws Exception {

        // Given

        ArgumentCaptor<Path> pathArgumentCaptor = ArgumentCaptor.forClass(Path.class);

        when(fileMock.getOriginalFilename()).thenReturn("my picture.jpg");
        when(dateUtilMock.getCurrentYearMonthDateString()).thenReturn("2018/8/29");
        when(openBlogSystemUtilMock.getOpenBlogHome()).thenReturn("/home/user/.openblog");
        when(uuidUtilMock.getUuid()).thenReturn("uuid123");

        // When
        storageService.store(fileMock);

        // Then
        verify(fileSystemFileWriterUtilMock).write(pathArgumentCaptor.capture(), any());

        if ("/".equals(File.separator)) {
            assertEquals("/home/user/.openblog/files/2018/8/29/uuid123_my-picture.jpg",
                    pathArgumentCaptor.getValue().toString());
        } else {
            assertEquals("\\home\\user\\.openblog\\files\\2018\\8\\29\\uuid123_my-picture.jpg",
                    pathArgumentCaptor.getValue().toString());
        }
    }

    @Test
    public void shouldMultipartFileBytesToBeWritten() throws Exception {

        // Given

        ArgumentCaptor<byte[]> bytesArgumentCaptor = ArgumentCaptor.forClass(byte[].class);

        when(fileMock.getOriginalFilename()).thenReturn("my picture.jpg");
        when(fileMock.getBytes()).thenReturn("pictureBytes".getBytes());

        when(dateUtilMock.getCurrentYearMonthDateString()).thenReturn("2018/8/29");
        when(openBlogSystemUtilMock.getOpenBlogHome()).thenReturn("/home/user/.openblog");
        when(uuidUtilMock.getUuid()).thenReturn("uuid123");

        // When
        storageService.store(fileMock);

        // Then
        verify(fileSystemFileWriterUtilMock).write(any(), bytesArgumentCaptor.capture());
        assertEquals("pictureBytes",
                new String(bytesArgumentCaptor.getValue(), "UTF-8"));
    }
}