package com.unloadbrain.blog.config;

import com.unloadbrain.blog.util.DateUtility;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DateConfig {

    @Bean
    public DateUtility dateUtility() {
        return new DateUtility();
    }
}
