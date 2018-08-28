package com.unloadbrain.blog.config;

import com.unloadbrain.blog.util.DateUtil;
import com.unloadbrain.blog.util.FileSystemFileWriterUtil;
import com.unloadbrain.blog.util.UuidUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppBeanConfig {

    @Bean
    public DateUtil dateUtility() {
        return new DateUtil();
    }

    @Bean
    public UuidUtil uuidConfig() {
        return new UuidUtil();
    }

    @Bean
    public FileSystemFileWriterUtil fileSystemFileWriterUtil() {
        return new FileSystemFileWriterUtil();
    }
}
