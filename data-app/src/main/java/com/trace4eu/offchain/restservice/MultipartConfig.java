package com.trace4eu.offchain.restservice;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(30));  // Max file size
        factory.setMaxRequestSize(DataSize.ofMegabytes(50));
        factory.setFileSizeThreshold(DataSize.ofMegabytes(50));
        return factory.createMultipartConfig();
    }
}