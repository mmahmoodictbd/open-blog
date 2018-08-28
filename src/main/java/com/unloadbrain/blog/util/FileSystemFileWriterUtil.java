package com.unloadbrain.blog.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileSystemFileWriterUtil {

    public void write(Path path, byte[] fileBytes) throws IOException {
        Files.write(path, fileBytes);
    }

    public void createDirectories(Path path) throws IOException {
        Files.createDirectories(path);
    }
}
