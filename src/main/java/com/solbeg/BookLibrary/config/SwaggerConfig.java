package com.solbeg.BookLibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.List;

import static com.solbeg.BookLibrary.config.SwaggerSettings.apiInfo;
import static com.solbeg.BookLibrary.utils.LibraryConstants.PROJECT_BASE_PACKAGE_PATH;
import static com.solbeg.BookLibrary.utils.LibraryConstants.WHITESPACE_DELIMITER;

@Configuration
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {

    public static final String BOOK_SERVICE_SWAGGER_TAG = "book service";
    public static final String AUTHOR_SERVICE_SWAGGER_TAG = "author service";
    public static final String TAG_SERVICE_SWAGGER_TAG = "tag service";
    public static final String ORDER_SERVICE_SWAGGER_TAG = "order service";
    public static final String USER_SERVICE_TAG = "user service";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
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

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .scopeSeparator(WHITESPACE_DELIMITER)
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

}
