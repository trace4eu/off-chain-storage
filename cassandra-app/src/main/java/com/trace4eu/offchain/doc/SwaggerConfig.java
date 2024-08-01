package com.trace4eu.offchain.doc;

import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
//                .apis(RequestHandlerSelectors.basePackage("com.trace4eu.offchain"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
//                .info(new Info()
//                        .title("Employee API")
//                        .version("v1")
//                        .description("API for managing employees"));
    }

}
