package com.searchengine.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class ApplicationConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public FileStorageConfig fileStorageConfig() {
        return new FileStorageConfig();
    }

}