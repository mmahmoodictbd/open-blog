package com.unloadbrain.blog.config;

import com.unloadbrain.blog.config.modelmapper.ModelMapperMapping;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ServiceLoader;

@Configuration
public class MappingConfig {

    @Bean
    public ModelMapper createModelMapper() {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ServiceLoader<ModelMapperMapping> modelMapperMappingLoader = ServiceLoader.load(ModelMapperMapping.class);
        for (ModelMapperMapping modelMapperMapping : modelMapperMappingLoader) {
            modelMapperMapping.appendToModelMapper(modelMapper);
        }

        return modelMapper;
    }

}
