package com.colourfulchina.bigan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfigurer {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.colourfulchina"))
                .paths(PathSelectors.any())
                .build()
//                .host("10.15.116.16:9999/bigan")
                .host("127.0.0.1:9999/bigan")
                ;
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder().title("客乐芙API接口")
                .version("1.0").build();
    }
}