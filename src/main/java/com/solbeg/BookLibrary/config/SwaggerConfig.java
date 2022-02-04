package com.solbeg.BookLibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.solbeg.BookLibrary.utils.LibraryConstants.PROJECT_BASE_PACKAGE_PATH;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    public static final String BOOK_SERVICE_SWAGGER_TAG = "book service";
    public static final String AUTHOR_SERVICE_SWAGGER_TAG = "author service";
    public static final String TAG_SERVICE_SWAGGER_TAG = "tag service";
    public static final String ORDER_SERVICE_SWAGGER_TAG = "order service";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage(PROJECT_BASE_PACKAGE_PATH))
                .paths(PathSelectors.any())
                .build()
                .tags(
                        new Tag(BOOK_SERVICE_SWAGGER_TAG, "CRUD operations for books"),
                        new Tag(AUTHOR_SERVICE_SWAGGER_TAG, "CRUD operations for authors"),
                        new Tag(TAG_SERVICE_SWAGGER_TAG, "CRUD operations for tags"),
                        new Tag(ORDER_SERVICE_SWAGGER_TAG, "CRUD operations for orders")
                )
                .apiInfo(SwaggerSettings.apiInfo());
    }
}
