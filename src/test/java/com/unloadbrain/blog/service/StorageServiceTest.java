package com.unloadbrain.blog.service;

import com.unloadbrain.blog.exception.FileNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class StorageServiceTest {

    private final StorageService storageService;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

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


}