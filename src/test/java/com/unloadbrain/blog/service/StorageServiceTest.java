package com.unloadbrain.blog.service;

import com.unloadbrain.blog.exception.BadUrlException;
import com.unloadbrain.blog.exception.FileNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StorageServiceImpl.class})
public class StorageServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final StorageService storageService;

    public StorageServiceTest() {
        this.storageService = new StorageServiceImpl();
    }

    @Test
    public void shouldThrowFileNotFoundExceptionWhenResourceNotExist() {

        // Given

        thrown.expect(FileNotFoundException.class);
        thrown.expectMessage("Requested file [hello] not found");

        // When

        storageService.loadFileAsResource("hello");

        // Then
        // Expect test to be passed.

    }

    @Test
    public void shouldThrowBadUrlExceptionWhenResourceUrlIsMalformed() throws Exception {

        // Given

        thrown.expect(BadUrlException.class);
        thrown.expectMessage("Provided URL [anything] is malformed.");

        whenNew(UrlResource.class).withAnyArguments().thenThrow(new MalformedURLException());

        // When

        storageService.loadFileAsResource("anything");

        // Then
        // Expect test to be passed.

    }
}